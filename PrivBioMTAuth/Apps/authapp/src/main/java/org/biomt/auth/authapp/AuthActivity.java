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
import cz.msebera.android.httpclient.message.BasicHeader;
import lib.zkp4.identity.commit.IdentityToken;
import lib.zkp4.identity.proof.IdentityProof;
import lib.zkp4.identity.proof.IdentityProofCreator;
import lib.zkp4.identity.util.Constants;
import lib.zkp4.identity.util.JSONIdentityProofEncoderDecoder;
import lib.zkp4.identity.util.JSONIdentityTokenEncoderDecoder;
import lib.zkp4.identity.util.JSONMiscEncoderDecoder;
import lib.zkp4.identity.util.Utilz;
import lib.zkp4.identity.verify.ProofInfo;


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

        final EditText identityText = (EditText) findViewById(R.id.identityText);
        final String identityValue = identityText.getText().toString();
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final String passwordValue = passwordText.getText().toString();
        //TODO: check if the above values exist, if not give an error.
        try {
            final String[] responseMsg = {null};
            /*//decode the identity token from string
            IdentityToken IDT = new JSONIdentityTokenEncoderDecoder().decodeIdentityToken(identityTokenString);

            IdentityProof proof = new IdentityProof();
            proof.setProofType(Constants.ZKP_I);
            proof.setIdentityTokenStringToBeProved(identityTokenString);
            PedersenPublicParams pedersenPublicParams = IDT.getPedersenParams();
            ZKPPedersenCommitment ZKPK = new ZKPPedersenCommitment(pedersenPublicParams);

            PedersenCommitment helperCommitment = ZKPK.createHelperProblem(null);
            proof.addHelperCommitment(helperCommitment.getCommitment());*/

            //decode the identity token from string
            final IdentityToken IDT = new JSONIdentityTokenEncoderDecoder().decodeIdentityToken(identityTokenString);

            final IdentityProof initialIdentityProof = new IdentityProofCreator().createInitialProofForZKPI(IDT);
            //encode the identity proof
            JSONObject encodedInitialIdentityProof = new JSONIdentityProofEncoderDecoder().encodeIdentityProof(initialIdentityProof);

            final Context appContext = this;

            AuthRESTClient spClient = new AuthRESTClient();
            //send the encoded identity proof and the username in an auth request
            Header[] headers = {new BasicHeader(Constants.USER_NAME, userName),
                    new BasicHeader(Constants.REQUEST_TYPE_NAME, Constants.REQ_ZKP_I_INITIAL)};
            spClient.post(this, spURL, headers, new StringEntity(encodedInitialIdentityProof.toString(), AuthConstants.ENCODING),
                    AuthConstants.CONTENT_TYPE_JSON, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Toast respToast = Toast.makeText(getApplicationContext(), String.valueOf(statusCode) +
                            response.toString(), Toast.LENGTH_LONG);
                    respToast.show();

                    try {
                        if (Constants.HTTP_CODE_OK == statusCode) {
                            ProofInfo proofInfo = new JSONMiscEncoderDecoder().decodeChallengeMessage(response.toString());
                            IdentityProof identityProof = new IdentityProofCreator().createProofForZKPI(proofInfo.getChallengeValue(),
                                    passwordValue, identityValue, IDT, initialIdentityProof);
                            JSONObject encodedIdentityProof = new JSONIdentityProofEncoderDecoder().encodeIdentityProof(identityProof);

                            AuthRESTClient spClient2 = new AuthRESTClient();
                            Header[] headers2 = {new BasicHeader(Constants.USER_NAME, userName),
                                    new BasicHeader(Constants.REQUEST_TYPE_NAME, Constants.REQ_ZKP_I_CHALLENGE_RESPONSE),
                                    new BasicHeader(Constants.SESSION_ID_NAME, proofInfo.getSessionID())};

                            spClient2.post(appContext, spURL, headers2, new StringEntity(encodedIdentityProof.toString(), AuthConstants.ENCODING),
                                    AuthConstants.CONTENT_TYPE_JSON, new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                                    Toast respToast = Toast.makeText(getApplicationContext(), String.valueOf(statusCode) +
                                            response.toString(), Toast.LENGTH_LONG);
                                    respToast.show();
                                }
                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
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
                        } else {
                            Intent responseIntent = new Intent(AuthConstants.ACTION_RESULT_AUTH_ZKP);
                            responseMsg[0] = response.toString();
                            responseIntent.putExtra(AuthConstants.INFO_CODE_ZKP_AUTH_RESP, responseMsg[0]);
                            setResult(Activity.RESULT_CANCELED, responseIntent);
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
