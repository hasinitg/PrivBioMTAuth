package org.biomt.auth.authapp;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

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

    public void post(Context context, String url, Header[] headers, HttpEntity entity, String contentType,
                     AsyncHttpResponseHandler responseHandler){
        client.post(context, url, headers, entity, contentType, responseHandler);
    }

}
