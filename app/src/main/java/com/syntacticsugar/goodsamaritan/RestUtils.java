package com.syntacticsugar.goodsamaritan;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by brandonyates on 4/5/16.
 */
public class RestUtils {

    //Brandon LocalHost
//    private static final String BASE_URL = "http://192.168.2.7:8080/";

    //Android LocalHost
    //private static final String BASE_URL = "http://10.0.2.2:8080/";

    //Production Server
    private static final String BASE_URL = "http://130.211.174.74:8080/";

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static SyncHttpClient syncclient = new SyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void syncget(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        syncclient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        System.out.println("url: " + getAbsoluteUrl(url));
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
