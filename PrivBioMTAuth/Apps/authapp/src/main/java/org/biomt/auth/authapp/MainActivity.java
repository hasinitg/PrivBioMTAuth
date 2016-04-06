package org.biomt.auth.authapp;

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

    public void onMainAuthButtonClicked(View v){
        Intent authIntent = new Intent(this, AuthActivity.class);
        authIntent.putExtra(AuthConstants.SP_URL_NAME, "http://128.10.120.195:8080/abcbank/account");
        //authIntent.putExtra(AuthConstants.SP_URL_NAME, "http://192.168.211.153:8080/abcbank/account");

        startActivityForResult(authIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String sessionId = data.getStringExtra("Session_Id");

        Toast myToast = Toast.makeText(getApplicationContext(),
                "Auth Success_"+sessionId, Toast.LENGTH_LONG);
        myToast.show();
    }
}
