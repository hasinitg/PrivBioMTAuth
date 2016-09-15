package org.biomt.auth.authapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import lib.zkp4.identity.proof.IdentityProof;
import lib.zkp4.identity.proof.IdentityProofCreator;
import lib.zkp4.identity.util.Constants;
import lib.zkp4.identity.util.JSONIdentityProofEncoderDecoder;
import lib.zkp4.identity.util.JSONMiscEncoderDecoder;
import lib.zkp4.identity.util.ZKP4IDException;
import lib.zkp4.identity.verify.ProofInfo;


public class AuthActivity extends AppCompatActivity {
    private String spURL = null;
    private String userName = null;
    private String identityTokenString = null;
    private String responseZKP_I_Initial = null;
    private String initialIdentityProofString = null;
    private String helperX;
    private String helperR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isConnected())) {
            Toast myToast = Toast.makeText(getApplicationContext(),
                    "Please connect to the internet first.", Toast.LENGTH_LONG);
            myToast.show();
        }

        Intent authRequest = getIntent();
        spURL = authRequest.getStringExtra(AuthConstants.SP_URL_NAME);
        userName = authRequest.getStringExtra(AuthConstants.USER_NAME_NAME);
        identityTokenString = authRequest.getStringExtra(AuthConstants.IDENTITY_TOKEN_STRING_NAME);
        responseZKP_I_Initial = authRequest.getStringExtra(AuthConstants.INFO_CODE_AUTH_RESP_ZKP_I_INITIAL);
        initialIdentityProofString = authRequest.getStringExtra(AuthConstants.INITIAL_ID_PROOF_NAME);
        helperX = authRequest.getStringExtra(AuthConstants.HELPER_X_NAME);
        helperR = authRequest.getStringExtra(AuthConstants.HELPER_R_NAME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_auth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAuthButtonClicked(View v) {

        final EditText identityText = (EditText) findViewById(R.id.auth_identityText);
        final String identityValue = identityText.getText().toString();
        final EditText passwordText = (EditText) findViewById(R.id.auth_passwordText);
        final String passwordValue = passwordText.getText().toString();
        //TODO: check if the above values exist, if not give an error.

        //Toast testToast = Toast.makeText(this, challengeString, Toast.LENGTH_LONG);
        //testToast.show();
        TrafficStats.setThreadStatsTag(0xF00C);
        try {
            //invoke the auth service with challenge-response.
            ProofInfo proofInfo = new JSONMiscEncoderDecoder().decodeChallengeMessage(responseZKP_I_Initial);
            IdentityProof initialIdentityProof = new JSONIdentityProofEncoderDecoder().decodeIdentityProof(initialIdentityProofString);
            //IdentityToken identityToken = new JSONIdentityTokenEncoderDecoder().decodeIdentityToken(identityTokenString);

            Log.i(Config.TAG_MAIN, Config.TAG_START_PROOF + Config.LOG_DELIMITTER + String.valueOf(System.currentTimeMillis()));

            IdentityProof identityProof = new IdentityProofCreator().createProofForZKPI(proofInfo.getChallengeValue(),
                    passwordValue, identityValue, identityTokenString, initialIdentityProof, helperX, helperR);
            JSONObject encodedIdentityProof = new JSONIdentityProofEncoderDecoder().encodeIdentityProof(identityProof);

            AuthRESTClient spClient = new AuthRESTClient();
            Header[] headers2 = {new BasicHeader(Constants.USER_NAME, userName),
                    new BasicHeader(Constants.REQUEST_TYPE_NAME, Constants.REQ_ZKP_I_CHALLENGE_RESPONSE),
                    new BasicHeader(Constants.SESSION_ID_NAME, proofInfo.getSessionID())};

            spClient.post(this, spURL, headers2, new StringEntity(encodedIdentityProof.toString(), AuthConstants.ENCODING),
                    AuthConstants.CONTENT_TYPE_JSON, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            /*Toast respToast = Toast.makeText(getApplicationContext(), String.valueOf(statusCode) +
                                    response.toString(), Toast.LENGTH_LONG);
                            respToast.show();*/
                            Log.i(Config.TAG_MAIN, Config.TAG_END_AUTH + Config.LOG_DELIMITTER + System.currentTimeMillis());

                            Intent authRespIntent = new Intent(AuthConstants.ACTION_RESULT_AUTH_ZKP);
                            authRespIntent.putExtra(AuthConstants.INFO_CODE_AUTH_RESP_ZKP_I_CHALLENGE_RESPONSE, response.toString());
                            setResult(Activity.RESULT_OK, authRespIntent);
                            finish();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            //print the response in a toast for now
                            //TODO: remember to assign errorResponse to responseMsg before returning the intent.
                            if (errorResponse != null) {
                                Toast errorToast = Toast.makeText(getApplicationContext(), errorResponse.toString(), Toast.LENGTH_LONG);
                                errorToast.show();
                            } else if (statusCode != 0) {
                                Toast errorToast = Toast.makeText(getApplicationContext(), String.valueOf(statusCode), Toast.LENGTH_LONG);
                                errorToast.show();
                            } else if (throwable != null && throwable.getMessage() != null) {
                                Toast errorToast = Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG);
                                errorToast.show();
                            }
                        }
                    });

        } catch (ZKP4IDException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            TrafficStats.clearThreadStatsTag();
        }

        /*client.get(testURLX.getText().toString(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                try {
                    sessionId[0] = (String) response.get("sessionId");
                    Intent resultIntent = new Intent(AuthConstants.ACTION_RESULT_AUTH_ZKP);

                    resultIntent.putExtra("Session_Id", sessionId[0]);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Intent resultIntent = new Intent(AuthConstants.ACTION_RESULT_AUTH_ZKP);
                resultIntent.putExtra("Session_Id", "Error");
                setResult(Activity.RESULT_OK,resultIntent);
                finish();
            }
        });*/
    }
}
