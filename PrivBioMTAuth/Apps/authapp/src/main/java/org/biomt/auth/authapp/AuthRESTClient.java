package org.biomt.auth.authapp;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by hasini on 3/16/16.
 */
public class AuthRESTClient {
    AsyncHttpClient client;

    public AuthRESTClient(){
        client = new AsyncHttpClient();
    }

    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.get(url, params, responseHandler);
    }

}
