package org.biomt.auth.clientapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void onAuthButtonClicked(View v){
        //create the implicit intent and call it with necessary inputs
        Intent authIntent = new Intent(ClientConstants.ACTION_AUTH_ZKP);
        //authIntent.setType("text/plain");
        authIntent.putExtra(ClientConstants.SP_URL_NAME, Config.SP_URL);

        //verify that the intended app exists.
        PackageManager pkgManager = getPackageManager();
        List intendedApps = pkgManager.queryIntentActivities(authIntent, PackageManager.MATCH_DEFAULT_ONLY);
        //TODO: verfify resolve info.
        boolean isSafeIntent = intendedApps.size()==1;

        //start intent for result
        if(isSafeIntent){
            startActivityForResult(authIntent, ClientConstants.ZKP_AUTH_REQUEST);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        //obtain the session from the returned activity
        String sessionId = data.getStringExtra("Session_Id");

        Toast myToast = Toast.makeText(getApplicationContext(),
                "Auth Success_"+sessionId, Toast.LENGTH_LONG);
        myToast.show();

        //contact SP using the session and obtain the account information

        //call the account page with the balance returned from SP.
    }
}
