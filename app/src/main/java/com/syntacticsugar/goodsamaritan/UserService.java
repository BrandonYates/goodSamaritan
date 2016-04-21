package com.syntacticsugar.goodsamaritan;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by brandonyates on 4/5/16.
 *
 * calls in this class will usually correspond 1 to 1 with the equivalent call in the backend UserService
 *
 */
public class UserService {

    OnJSONResponseCallback callback = new OnJSONResponseCallback();
    //parameterized user Constructor
    public void createUser(String firstName, String lastName, String emailAddress, String password) throws JSONException {

        //build parameters
        RequestParams params = new RequestParams();
        params.add("firstName", firstName);
        params.add("lastName", lastName);
        params.add("emailAddress", emailAddress);
        params.add("password", password);

        System.out.println("CREATE USER!");
        System.out.println("params: " + params.toString());

        //build rest call

        RestUtils.post("createUser/params", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("##############");
                System.out.println("onSuccess");
                try {

                    String response = new String(responseBody, "UTF-8");

                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    System.out.println(obj.toString());

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("EXCEPTION!");
                    e.printStackTrace();
                }
                System.out.println("##############");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String response = new String(responseBody, "UTF-8");
                    System.out.println(statusCode + ": " + response);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void signIn (String emailAddress, String password) throws JSONException {
        System.out.println("SIGN IN CALLED");
        RequestParams params = new RequestParams();

        params.add("emailAddress", emailAddress);
        params.add("password", password);

        RestUtils.post("authenticate", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("##############");
                System.out.println("onSuccess");
                System.out.println("statusCode: " + statusCode);
                System.out.println("headers: " + headers.toString());
                try {

                    String response = new String(responseBody, "UTF-8");
                    System.out.println("response: " + response);

                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    if(statusCode != 200) {
                        System.out.println("##############");
                        System.out.println("# error : " + statusCode);
                        System.out.println("##############");
                        throw new Exception();
                    }

                    System.out.println(obj.toString());

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("##############");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String response = new String(responseBody, "UTF-8");
                    System.out.println(statusCode + ": " + response);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public JSONObject findUserById (String userId) throws JSONException {
        RequestParams params = new RequestParams();

        params.add("id", userId);

        RestUtils.get("findUserById", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println(response.toString());

                callback.onJSONResponse(true, response);
//                String response = new String(responseBody, "UTF-8");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                System.out.println(timeline.toString());
            }
        });

        return callback.getObject();
    }

    public void findUserByEmail (String emailAddress) throws JSONException {
        RequestParams params = new RequestParams();

        params.add("emailAddress", emailAddress);

        RestUtils.get("findUserByEmail", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println(response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                System.out.println(timeline.toString());
            }
        });
    }

    public void hello () {

        RestUtils.get("/hello", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println(response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                System.out.println(timeline.toString());
            }
        });
    }

//    public User objectToUser (JSONObject object) {
//
//        return new User();
//    }
}
