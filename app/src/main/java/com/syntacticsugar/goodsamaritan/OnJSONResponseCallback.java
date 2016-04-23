package com.syntacticsugar.goodsamaritan;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by brandonyates on 4/21/16.
 */
public class OnJSONResponseCallback {

    JSONObject object;
    JSONArray array;

    public void onJSONResponse(boolean success, JSONObject response) {

        if(success) {
            object = response;
        } else {
            object = null;
        }
    }

    public void onJSONArrayResponse(boolean success, JSONArray response) {
        if(success) {
            array = response;
        } else {
            array = null;
        }
    }

    public JSONObject getObject() { return object; }

    public JSONArray getArray() { return array; }
}
