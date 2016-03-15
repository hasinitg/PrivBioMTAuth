package org.biomt.auth.authapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Intent authRequest = getIntent();
        String SP_URL = authRequest.getStringExtra(AuthConstants.SP_URL_NAME);
        EditText identityText = (EditText) findViewById(R.id.identityText);
        identityText.setText(SP_URL);

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
        Intent resultIntent = new Intent(AuthConstants.RESULT_AUTH_ZKP);
        resultIntent.putExtra("Session_Id", "1223456hgdfgh");
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }
}
