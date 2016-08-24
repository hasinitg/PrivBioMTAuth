package org.biomt.auth.authapp;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by hasini on 8/22/16.
 */
public class Util {
    public void checkNetworkConnectivity(Activity activity){
        ConnectivityManager connMgr = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (!(networkInfo!=null && networkInfo.isConnected())){
            Toast myToast = Toast.makeText(activity.getApplicationContext(),
                    "Please connect to the internet first.", Toast.LENGTH_LONG);
            myToast.show();
        }

    }
}
