package org.biomt.auth.authapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import lib.zkp4.identity.commit.IdentityToken;
import lib.zkp4.identity.util.JSONIdentityTokenEncoderDecoder;

/*This activity started by the Android system through an implicit intent passed by another app -
* specifically, any SP client application.*/
public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**check if already enrolled for the given user name and SP NAME pair and if IDT and other artifacts exists.
         If so, redirect to authenticate window, if not, redirect to enroll window.**/

        //obtain the intent and the passed data
        Intent authRequest = getIntent();
        String spURL = authRequest.getStringExtra(AuthConstants.SP_URL_NAME);
        String userName = authRequest.getStringExtra(AuthConstants.USER_NAME_NAME);

        //invoke the enroll activity for now.
        Intent enrollActivityIntent = new Intent(this, EnrollmentActivity.class);
        enrollActivityIntent.putExtra(AuthConstants.SP_URL_NAME, spURL);
        enrollActivityIntent.putExtra(AuthConstants.USER_NAME_NAME, userName);
        startActivityForResult(enrollActivityIntent, AuthConstants.REQUEST_CODE_ENROLL);

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
                //decode the response, save the artifacts and redirects to authentication
                try {
                    Intent responseIntent = getIntent();
                    String responseString = responseIntent.getStringExtra(AuthConstants.INFO_CODE_ENROLL_RESP);
                    IdentityToken IDT = new JSONIdentityTokenEncoderDecoder().decodeIdentityToken(responseString);
                    
                } catch (Exception e) {
                    //return to client app with the error
                    Intent responseErrorIntent = new Intent(AuthConstants.ACTION_RESULT_AUTH_ZKP);
                    responseErrorIntent.putExtra(AuthConstants.INFO_CODE_ZKP_AUTH_RESP,
                            "Error in decoding the enrollment response: " + e.getMessage());
                    setResult(Activity.RESULT_CANCELED, responseErrorIntent);
                    //e.printStackTrace();
                }

            } else if (Activity.RESULT_CANCELED == resultCode) {
                //redirects to the client app with the error message
            }

        } else if (AuthConstants.REQUEST_CODE_ZKP_AUTH == requestCode) {
            //if it is from auth activity and if it is a success
            if (Activity.RESULT_OK == resultCode) {

            } else if (Activity.RESULT_CANCELED == resultCode) {

            }
        }


    }
}
