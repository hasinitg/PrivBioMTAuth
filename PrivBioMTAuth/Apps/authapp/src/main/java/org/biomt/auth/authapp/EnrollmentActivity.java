package org.biomt.auth.authapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.crypto.lib.CryptoLibConstants;
import org.crypto.lib.util.CryptoUtil;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import lib.zkp4.identity.commit.IDTRequest;
import lib.zkp4.identity.util.JSONIDTRequestEncoderDecoder;
import lib.zkp4.identity.util.ZKP4IDException;

public class EnrollmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        Intent enrollActivityIntent = getIntent();
        String spURL = enrollActivityIntent.getStringExtra(AuthConstants.SP_URL_NAME);
        String userName = enrollActivityIntent.getStringExtra(AuthConstants.USER_NAME_NAME);

        //set the values in the UI components
        EditText fromText = (EditText) findViewById(R.id.editTextFrom);
        fromText.setText(userName);

        EditText toText = (EditText) findViewById(R.id.editTextTo);
        toText.setText(spURL);

        EditText idpURLText = (EditText) findViewById(R.id.editTextIDPURL);
        idpURLText.setText(Config.IDP_URL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_enrollment, menu);
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

    public void onEnrollButtonClicked(View v){
        try {
            //get the information from the UI
            EditText identityValue = (EditText) findViewById(R.id.editTextIdentity);
            EditText passwordValue = (EditText) findViewById(R.id.editTextPassword);
            EditText fromValue = (EditText) findViewById(R.id.editTextFrom);
            EditText toValue = (EditText) findViewById(R.id.editTextTo);
            EditText idpURLValue = (EditText) findViewById(R.id.editTextIDPURL);

            //create the IDT request
            IDTRequest idtRequest = new IDTRequest();
            idtRequest.setAttributeName(AuthConstants.BIOMETRIC_ATTRIBUTE_NAME);
            idtRequest.setAttributeValue(identityValue.getText().toString());
            //TODO: set the appropriate length of the hashed password which will be used to derive multiple secrets.
            //Make sure IDP takes care of deriving multiple secrets as ZKP4IDLib doesn't handle the special case for biometrics.
            idtRequest.setEncryptedSecret(CryptoUtil.getCommittableThruHash(passwordValue.getText().toString(),
                    CryptoLibConstants.SECRET_BIT_LENGTH).toString());
            idtRequest.setFromField(fromValue.getText().toString());
            idtRequest.setToField(toValue.getText().toString());

            //encode IDT request
            String encodedIDTRequest = new JSONIDTRequestEncoderDecoder().encodeIDTRequest(idtRequest).toString();

            //invoke the client for the post request
            AuthRESTClient idpClient = new AuthRESTClient();
            //TODO: send the user name (and the authentication details) with the IDP in the headers
            idpClient.post(this, idpURLValue.getText().toString(), null, new StringEntity(encodedIDTRequest,
                            AuthConstants.ENCODING), AuthConstants.CONTENT_TYPE_JSON, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Intent responseIntent = new Intent(AuthConstants.ACTION_RESULT_ENROLLMENT);
                    responseIntent.putExtra(AuthConstants.INFO_CODE_ENROLL_RESP, response.toString());
                    setResult(Activity.RESULT_OK, responseIntent);
                    finish();
                    /*//print the response in a toast for now
                    Toast responseToast = Toast.makeText(getApplicationContext(), response.toString(),
                            Toast.LENGTH_LONG);
                    responseToast.show();*/
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                      JSONObject errorResponse) {
                    //print the response in a toast for now
                    Toast errorToast = Toast.makeText(getApplicationContext(), errorResponse.toString(),
                            Toast.LENGTH_LONG);
                    errorToast.show();
                }
            });

        } catch (NoSuchAlgorithmException e) {
            Toast errorToast = Toast.makeText(getApplicationContext(),
                    "Error in creating IDT request.", Toast.LENGTH_LONG);
            errorToast.show();
        } catch (ZKP4IDException e) {
            Toast errorToast = Toast.makeText(getApplicationContext(),
                    "Error in encoding IDT request.", Toast.LENGTH_LONG);
            errorToast.show();
        }
    }
}
