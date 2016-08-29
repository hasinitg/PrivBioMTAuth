package org.biomt.auth.authapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.crypto.lib.commitments.pedersen.PedersenCommitment;
import org.crypto.lib.commitments.pedersen.PedersenPublicParams;
import org.crypto.lib.exceptions.CryptoAlgorithmException;
import org.crypto.lib.zero.knowledge.proof.ZKPPedersenCommitment;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import lib.zkp4.identity.commit.IdentityToken;
import lib.zkp4.identity.proof.IdentityProof;
import lib.zkp4.identity.util.Constants;
import lib.zkp4.identity.util.JSONIdentityProofEncoderDecoder;
import lib.zkp4.identity.util.JSONIdentityTokenEncoderDecoder;
import lib.zkp4.identity.util.Utilz;


public class AuthActivity extends AppCompatActivity {
    private String spURL = null;
    private String userName = null;
    private String identityTokenString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
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

        AuthRESTClient client = new AuthRESTClient();

        EditText identityText = (EditText) findViewById(R.id.identityText);
        EditText passwordText = (EditText) findViewById(R.id.passwordText);
        try {
            final String[] responseMsg = {null};
            //decode the identity token from string
            IdentityToken IDT = new JSONIdentityTokenEncoderDecoder().decodeIdentityToken(identityTokenString);

            IdentityProof proof = new IdentityProof();
            proof.setProofType(Constants.ZKP_I);

            PedersenPublicParams pedersenPublicParams = IDT.getPedersenParams();
            ZKPPedersenCommitment ZKPK = new ZKPPedersenCommitment(pedersenPublicParams);

            PedersenCommitment helperCommitment = ZKPK.createHelperProblem(null);
            proof.addHelperCommitment(helperCommitment.getCommitment());

            //encode the identity proof
            JSONObject encodedIdentityProof = new JSONIdentityProofEncoderDecoder().encodeIdentityProof(proof);
            AuthRESTClient spClient = new AuthRESTClient();
            //send the encoded identity proof and the username in an auth request
            spClient.post(this, spURL, null, new StringEntity(encodedIdentityProof.toString(), AuthConstants.ENCODING),
                    AuthConstants.CONTENT_TYPE_JSON, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //print the response in a toast for now
                    /*Toast responseToast = Toast.makeText(getApplicationContext(), response.toString(),
                    Toast.LENGTH_LONG);
                    responseToast.show();*/
                    Intent responseIntent = new Intent(AuthConstants.ACTION_RESULT_ENROLLMENT);
                    responseMsg[0] = response.toString();
                    responseIntent.putExtra(AuthConstants.INFO_CODE_ENROLL_RESP, responseMsg[0]);
                    setResult(Activity.RESULT_OK, responseIntent);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                      JSONObject errorResponse) {
                    //print the response in a toast for now
                    //TODO: remember to assign errorResponse to responseMsg before returning the intent.
                    if (errorResponse != null) {
                        Toast errorToast = Toast.makeText(getApplicationContext(), errorResponse.toString(),
                                Toast.LENGTH_LONG);
                        errorToast.show();
                    } else if (statusCode != 0) {
                        Toast errorToast = Toast.makeText(getApplicationContext(), String.valueOf(statusCode),
                                Toast.LENGTH_LONG);
                        errorToast.show();
                    } else if (throwable != null && throwable.getMessage() != null) {
                        Toast errorToast = Toast.makeText(getApplicationContext(), throwable.getMessage(),
                                Toast.LENGTH_LONG);
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
