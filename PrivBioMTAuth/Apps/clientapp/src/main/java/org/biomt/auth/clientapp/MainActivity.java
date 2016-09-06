package org.biomt.auth.clientapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText defaultSPURL = (EditText) findViewById(R.id.editTextSPURL);
        defaultSPURL.setText(Config.SP_URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        //check if the network connectivity is there
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isConnected())) {
            Toast myToast = Toast.makeText(getApplicationContext(),
                    "Please connect to the internet first.", Toast.LENGTH_LONG);
            myToast.show();
        }

        //create the implicit intent and call it with necessary inputs
        Intent authIntent = new Intent(ClientConstants.ACTION_AUTH_ZKP);

        //add inputs to the intent
        EditText spURL = (EditText) findViewById(R.id.editTextSPURL);
        //TODO: check if the sp url is null
        authIntent.putExtra(ClientConstants.SP_URL_NAME, spURL.getText().toString());

        EditText userName = (EditText) findViewById(R.id.editTextUserName);
        authIntent.putExtra(ClientConstants.USER_NAME_NAME, userName.getText().toString());

        //verify that the intended app exists.
        PackageManager pkgManager = getPackageManager();
        List intendedApps = pkgManager.queryIntentActivities(authIntent, PackageManager.MATCH_DEFAULT_ONLY);
        //TODO: verfify resolve info.
        boolean isSafeIntent = intendedApps.size() == 1;

        //start intent for result
        if (isSafeIntent) {
            startActivityForResult(authIntent, ClientConstants.REQUEST_CODE_ZKP_AUTH);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            //obtain the session from the returned activity
            String sessionId = data.getStringExtra(ClientConstants.SESSION_ID_NAME);

            Toast myToast = Toast.makeText(getApplicationContext(),
                    "Auth Success: " + sessionId, Toast.LENGTH_LONG);
            myToast.show();
        } else {
            Toast myToast = Toast.makeText(getApplicationContext(),
                    "Auth Failure.. ", Toast.LENGTH_LONG);
            myToast.show();
        }

        //contact SP using the session and obtain the account information

        //call the account page with the balance returned from SP.
    }
}
