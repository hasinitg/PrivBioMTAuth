package org.biomt.auth.authapp;

import android.app.Activity;
import android.content.Intent;
import android.net.TrafficStats;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.crypto.lib.exceptions.CryptoAlgorithmException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import lib.zkp4.identity.commit.IdentityToken;
import lib.zkp4.identity.proof.IdentityProof;
import lib.zkp4.identity.proof.IdentityProofCreator;
import lib.zkp4.identity.proof.IdentityProofPackage;
import lib.zkp4.identity.util.Constants;
import lib.zkp4.identity.util.JSONIdentityProofEncoderDecoder;
import lib.zkp4.identity.util.JSONIdentityTokenEncoderDecoder;

public class AuthInitialActivity extends AppCompatActivity {
    private String userName = null;
    private String spURL = null;
    private String identityTokenString = null;

    /*@Override
    protected void onStop() {
        super.onStop();
        onDestroy();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent initialAuthRequest = getIntent();
        userName = initialAuthRequest.getStringExtra(AuthConstants.USER_NAME_NAME);
        spURL = initialAuthRequest.getStringExtra(AuthConstants.SP_URL_NAME);
        identityTokenString = initialAuthRequest.getStringExtra(AuthConstants.IDENTITY_TOKEN_STRING_NAME);
        requestZKPAuthInitial();
        /*There is nothing to show in the AuthInitialActivity.*/
        //setContentView(R.layout.activity_auth_initial);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_auth_initial, menu);
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

    private void requestZKPAuthInitial() {
        TrafficStats.setThreadStatsTag(0xF00B);
        try {
            //decode the identity token from string
            IdentityToken IDT = new JSONIdentityTokenEncoderDecoder().decodeIdentityToken(identityTokenString);

            Log.i(Config.TAG_MAIN, Config.TAG_START_AUTH + Config.LOG_DELIMITTER + System.currentTimeMillis());

            IdentityProofPackage identityProofPackage = new IdentityProofCreator().createInitialProofForZKPI(IDT);
            IdentityProof initialIdentityProof = identityProofPackage.getIdentityProof();

            final String helperX = identityProofPackage.getHelperX().toString();
            final String helperR = identityProofPackage.getHelperR().toString();

            //encode the identity proof, (this needs to be returned along with success result).
            JSONObject encodedInitialIdentityProof = new JSONIdentityProofEncoderDecoder().encodeIdentityProof(initialIdentityProof);
            final String encodedInitialIdentityProofString = encodedInitialIdentityProof.toString();

            AuthRESTClient spClient = new AuthRESTClient();
            //send the encoded identity proof and the username in an auth request
            Header[] headers = {new BasicHeader(Constants.USER_NAME, userName),
                    new BasicHeader(Constants.REQUEST_TYPE_NAME, Constants.REQ_ZKP_I_INITIAL)};

            spClient.post(this, spURL, headers, new StringEntity(encodedInitialIdentityProofString, AuthConstants.ENCODING),
                    AuthConstants.CONTENT_TYPE_JSON, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    Log.i(Config.TAG_MAIN, Config.TAG_CHALLENGE_RECEIVED + Config.LOG_DELIMITTER + String.valueOf(System.currentTimeMillis()));

                    /*Toast respToast = Toast.makeText(getApplicationContext(), String.valueOf(statusCode) +
                            response.toString(), Toast.LENGTH_LONG);
                    respToast.show();*/

                    String respString = response.toString();

                    try {
                        if (Constants.HTTP_CODE_OK == statusCode) {
                            Intent initialAuthRespIntent = new Intent(AuthConstants.ACTION_RESULT_AUTH_ZKP);
                            initialAuthRespIntent.putExtra(AuthConstants.INFO_CODE_AUTH_RESP_ZKP_I_INITIAL, respString);
                            initialAuthRespIntent.putExtra(AuthConstants.INITIAL_ID_PROOF_NAME, encodedInitialIdentityProofString);
                            initialAuthRespIntent.putExtra(AuthConstants.HELPER_X_NAME, helperX);
                            initialAuthRespIntent.putExtra(AuthConstants.HELPER_R_NAME, helperR);
                            setResult(Activity.RESULT_OK, initialAuthRespIntent);
                            finish();
                        } else {

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        } catch (CryptoAlgorithmException e) {
            //TODO: create result intent with error and return
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
