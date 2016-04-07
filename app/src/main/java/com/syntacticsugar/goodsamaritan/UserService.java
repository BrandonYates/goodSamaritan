package com.syntacticsugar.goodsamaritan;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by brandonyates on 4/5/16.
 *
 * calls in this class will usually correspond 1 to 1 with the equivalent call int he backend UserService
 *
 */
public class UserService {

    //parameterized user Constructor
    public void createUser(String firstName, String lastName, String emailAddress, String password) throws JSONException {

        //build parameters
        RequestParams params = new RequestParams();
        params.add("firstName", firstName);
        params.add("lastName", lastName);
        params.add("emailAddress", emailAddress);
        params.add("password", password);

        //build rest call
        RestUtils.post("createUser/params", params, new JsonHttpResponseHandler() {
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
}
