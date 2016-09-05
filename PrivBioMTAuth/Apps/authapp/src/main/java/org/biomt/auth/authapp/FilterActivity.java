package org.biomt.auth.authapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import lib.zkp4.identity.commit.IdentityToken;
import lib.zkp4.identity.util.Constants;
import lib.zkp4.identity.util.JSONIdentityTokenEncoderDecoder;
import lib.zkp4.identity.util.JSONMiscEncoderDecoder;
import lib.zkp4.identity.verify.AuthResult;

/*This activity started by the Android system through an implicit intent passed by another app -
* specifically, any SP client application. This routes the request to either enrollment activity or
* auth activity as appropriate.*/
public class FilterActivity extends AppCompatActivity {
    private String spURL = null;
    private String userName = null;
    private String identityTokenString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**check if already enrolled for the given user name and SP NAME pair and if IDT and other artifacts exists.
         If so, redirect to authenticate window, if not, redirect to enroll window.**/

        //obtain the intent and retrieve the passed data
        Intent authRequest = getIntent();
        spURL = authRequest.getStringExtra(AuthConstants.SP_URL_NAME);
        userName = authRequest.getStringExtra(AuthConstants.USER_NAME_NAME);

        //hand over db access to an async task
        new IdentityTokenReadTask(this).execute();

        /*Since the filter activity acts as the controller, in model-view-controller pattern, we do not display it.*/
        //setContentView(R.layout.activity_filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /* Waiting for the result from EnrollActivity or AuthActivity which are differentiated from the
        request code */
        // if it is from enroll activity:
        if (AuthConstants.REQUEST_CODE_ENROLL == requestCode) {
            // and if it is a success
            if (Activity.RESULT_OK == resultCode) {
                try {
                    //extract the response, save the artifacts and redirects to authentication
                    identityTokenString = data.getStringExtra(AuthConstants.INFO_CODE_ENROLL_RESP);
                    Toast respToast = Toast.makeText(this, identityTokenString, Toast.LENGTH_LONG);
                    respToast.show();
                    new IdentityTokenWriteTask(this).execute();

                } catch (Exception e) {
                    //return to client app with the error
                    Intent responseErrorIntent = new Intent(AuthConstants.ACTION_RESULT_AUTH_ZKP);
                    responseErrorIntent.putExtra(AuthConstants.INFO_CODE_AUTH_RESP,
                            "Error in processing the enrollment response: " + e.getMessage());
                    setResult(Activity.RESULT_CANCELED, responseErrorIntent);
                    //e.printStackTrace();
                }

            } else if (Activity.RESULT_CANCELED == resultCode) {
                //redirects to the client app with the error message
                //TODO: complete this after completing error handling in the enrollment activity.
                Intent responseErrorIntent = new Intent(AuthConstants.ACTION_RESULT_AUTH_ZKP);
                responseErrorIntent.putExtra(AuthConstants.INFO_CODE_AUTH_RESP,
                        "Error in processing the enrollment response: ");
                setResult(Activity.RESULT_CANCELED, responseErrorIntent);
            }

        } else if (AuthConstants.REQUEST_CODE_ZKP_INITIAL == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                String initialAuthResp = data.getStringExtra(AuthConstants.INFO_CODE_AUTH_RESP_ZKP_I_INITIAL);
                String initialIDProof = data.getStringExtra(AuthConstants.INITIAL_ID_PROOF_NAME);
                String helperX = data.getStringExtra(AuthConstants.HELPER_X_NAME);
                String helperR = data.getStringExtra(AuthConstants.HELPER_R_NAME);
                invokeAuthActivityForZKP_I(initialAuthResp, initialIDProof, helperX, helperR);
            } else {

            }
        } else if (AuthConstants.REQUEST_CODE_ZKP_CHALLENGE_RESPONSE == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                //decode the authentication result and pass it back to the client application
                try {
                    String authResp = data.getStringExtra(AuthConstants.INFO_CODE_AUTH_RESP_ZKP_I_CHALLENGE_RESPONSE);
                    AuthResult authResult = new JSONMiscEncoderDecoder().decodeAuthResult(authResp);

                    Intent authResultIntent = new Intent(AuthConstants.ACTION_RESULT_AUTH_ZKP);
                    if (authResult.getAuthResult()) {
                        authResultIntent.putExtra(Constants.SESSION_ID_NAME, authResult.getSessionID());
                        setResult(Activity.RESULT_OK, authResultIntent);
                        finish();

                    } else {
                        setResult(Activity.RESULT_CANCELED, authResultIntent);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    protected void invokeAuthActivityForZKP_I(String responseZKP_Initial, String encodedInitialIdentityProofString,
                                              String helperX, String helperR) {
        Intent authActivityIntent = new Intent(this, AuthActivity.class);
        authActivityIntent.putExtra(AuthConstants.USER_NAME_NAME, userName);
        authActivityIntent.putExtra(AuthConstants.SP_URL_NAME, spURL);
        authActivityIntent.putExtra(AuthConstants.IDENTITY_TOKEN_STRING_NAME, identityTokenString);
        authActivityIntent.putExtra(AuthConstants.INFO_CODE_AUTH_RESP_ZKP_I_INITIAL, responseZKP_Initial);
        authActivityIntent.putExtra(AuthConstants.INITIAL_ID_PROOF_NAME, encodedInitialIdentityProofString);
        authActivityIntent.putExtra(AuthConstants.HELPER_X_NAME, helperX);
        authActivityIntent.putExtra(AuthConstants.HELPER_R_NAME, helperR);
        startActivityForResult(authActivityIntent, AuthConstants.REQUEST_CODE_ZKP_CHALLENGE_RESPONSE);
    }

    protected void continueWithZKP_I_Auth() {
        //invoke AuthInitialActivity to send the initial ZKP_I request to the SP.
        Intent authInitialIntent = new Intent(this, AuthInitialActivity.class);
        authInitialIntent.putExtra(AuthConstants.USER_NAME_NAME, userName);
        authInitialIntent.putExtra(AuthConstants.SP_URL_NAME, spURL);
        authInitialIntent.putExtra(AuthConstants.IDENTITY_TOKEN_STRING_NAME, identityTokenString);
        startActivityForResult(authInitialIntent, AuthConstants.REQUEST_CODE_ZKP_INITIAL);
        /*String responseZKP_I_Initial = requestZKPAuthInitial(identityTokenString);
        if (responseZKP_I_Initial.contains(AuthConstants.SUCCESS_NAME)) {
            String challengeMessage = responseZKP_I_Initial.split(":")[1];
            invokeAuthActivityForZKP_I(identityTokenString, challengeMessage);
        } else {
            //TODO: return intent result to the client app with the error message
        }*/
    }

    /*Write to the database asynchronously, and upon successful write, invoke the auth activity.*/
    private class IdentityTokenWriteTask extends AsyncTask<String, Void, String> {
        private Context appContext = null;
        private IdentityToken identityToken = null;

        public IdentityTokenWriteTask(Context context) {
            this.appContext = context;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //identityTokenString = params[0];
                identityToken = new JSONIdentityTokenEncoderDecoder().decodeIdentityToken(identityTokenString);
                DatabaseAccessManager databaseAccessManager = new DatabaseAccessManager();
                String insertedRowID = databaseAccessManager.writeToIdentityTokenTable(appContext, identityTokenString, identityToken);
                return AuthConstants.SUCCESS_NAME + "IDT saved successfully." + insertedRowID;
            } catch (Exception e) {
                return AuthConstants.ERROR_NAME + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Show result of the database write task in a toast.
            Toast resultToast = Toast.makeText(appContext, result, Toast.LENGTH_LONG);
            resultToast.show();
            //Create an intent and invoke the AuthActivity.
            if (result.contains(AuthConstants.SUCCESS_NAME)) {
                continueWithZKP_I_Auth();

                /*Intent authActivityIntent = new Intent(appContext, AuthActivity.class);
                authActivityIntent.putExtra(AuthConstants.SP_URL_NAME, spURL);
                authActivityIntent.putExtra(AuthConstants.USER_NAME_NAME, userName);
                authActivityIntent.putExtra(AuthConstants.IDENTITY_TOKEN_STRING_NAME, identityTokenString);

                ((Activity) appContext).startActivityForResult(authActivityIntent, AuthConstants.REQUEST_CODE_ZKP_INITIAL);*/
            } else {
                //TODO: handle the error results.
            }
        }
    }

    /*Read from the database - if an IDT exists, forward to AuthActivity with that, else, forward to EnrollActivity*/
    private class IdentityTokenReadTask extends AsyncTask<String, Void, String> {
        private Context appContext = null;

        public IdentityTokenReadTask(Context context) {
            appContext = context;
        }

        @Override
        protected String doInBackground(String... params) {
            DatabaseAccessManager databaseAccessManager = new DatabaseAccessManager();
            identityTokenString = databaseAccessManager.readFromIdentityTokenTable(appContext,
                    userName, spURL);
            return identityTokenString;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                continueWithZKP_I_Auth();
                /*//invoke the auth activity, upon successful retrieval
                Intent authActivityIntent = new Intent(appContext, AuthActivity.class);
                authActivityIntent.putExtra(AuthConstants.SP_URL_NAME, spURL);
                authActivityIntent.putExtra(AuthConstants.USER_NAME_NAME, userName);
                authActivityIntent.putExtra(AuthConstants.IDENTITY_TOKEN_STRING_NAME, result);
                ((Activity) appContext).startActivityForResult(authActivityIntent, AuthConstants.REQUEST_CODE_ZKP_INITIAL);*/

            } else {
                // if no identity token found,invoke the enroll activity to obtain an identity token
                Intent enrollActivityIntent = new Intent(appContext, EnrollmentActivity.class);
                enrollActivityIntent.putExtra(AuthConstants.SP_URL_NAME, spURL);
                enrollActivityIntent.putExtra(AuthConstants.USER_NAME_NAME, userName);
                ((Activity) appContext).startActivityForResult(enrollActivityIntent, AuthConstants.REQUEST_CODE_ENROLL);
            }
        }
    }
}
