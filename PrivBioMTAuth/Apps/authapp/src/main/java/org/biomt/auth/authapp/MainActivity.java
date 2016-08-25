package org.biomt.auth.authapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

    public void onMainAuthButtonClicked(View v){
        /*Intent authIntent = new Intent(this, AuthActivity.class);
        authIntent.putExtra(AuthConstants.SP_URL_NAME, Config.TEST_URL);
        //authIntent.putExtra(AuthConstants.SP_URL_NAME, "http://128.10.120.195:8080/abcbank/account");
        //authIntent.putExtra(AuthConstants.SP_URL_NAME, "http://192.168.211.153:8080/abcbank/account");

        startActivityForResult(authIntent, AuthConstants.REQUEST_CODE_ZKP_AUTH);*/

        //invoke filter activity just like it is done by the client app, for the debug purposes
        Intent enrollmentIntent = new Intent(this, FilterActivity.class);
        enrollmentIntent.putExtra(AuthConstants.SP_URL_NAME, "whatever");
        enrollmentIntent.putExtra(AuthConstants.USER_NAME_NAME, "whatever");
        startActivityForResult(enrollmentIntent, AuthConstants.REQUEST_CODE_ENROLL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //String sessionId = data.getStringExtra("Session_Id");

        Toast myToast = Toast.makeText(getApplicationContext(),
                "whatever", Toast.LENGTH_LONG);
        myToast.show();
    }
}
