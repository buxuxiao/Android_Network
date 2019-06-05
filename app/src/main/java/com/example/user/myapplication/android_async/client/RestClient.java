package com.example.user.myapplication.android_async.client;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by user on 2018/4/22.
 */

public class RestClient {
    private static final String BASR_URL="http://101.1.3.167:7008/Liems/webservice/";

    private static AsyncHttpClient client=new AsyncHttpClient();

    public static void get(String action, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.get(getUrl(action),params,responseHandler);
    }

    public static void post(String url,RequestParams params,AsyncHttpResponseHandler responseHandler){
        client.post(getUrl(url),params,responseHandler);
    }

    private static String getUrl(String action){
        return BASR_URL+action;
    }
}
