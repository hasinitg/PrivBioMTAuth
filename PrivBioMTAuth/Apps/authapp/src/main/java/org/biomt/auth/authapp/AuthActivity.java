package org.biomt.auth.authapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class AuthActivity extends AppCompatActivity {
    private  String spURL = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Intent authRequest = getIntent();
        spURL = authRequest.getStringExtra(AuthConstants.SP_URL_NAME);

        EditText identityText = (EditText) findViewById(R.id.identityText);
        identityText.setText(spURL);

        //perform ZKP with SP.

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

    public void onAuthButtonClicked(View v){
        AuthRESTClient client = new AuthRESTClient();
        final String[] sessionId = {null};
        client.get(spURL, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                try {
                    sessionId[0] = (String) response.get("sessionId");
                    Intent resultIntent = new Intent(AuthConstants.RESULT_AUTH_ZKP);
                    resultIntent.putExtra("Session_Id", sessionId[0]);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Intent resultIntent = new Intent(AuthConstants.RESULT_AUTH_ZKP);
                resultIntent.putExtra("Session_Id", "Error");
                setResult(Activity.RESULT_OK,resultIntent);
                finish();
            }
        });

    }
}