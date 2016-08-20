package org.biomt.auth.authapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

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
}
