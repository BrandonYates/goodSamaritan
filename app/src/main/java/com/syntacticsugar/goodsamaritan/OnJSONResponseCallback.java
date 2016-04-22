package com.syntacticsugar.goodsamaritan;

import org.json.JSONObject;

/**
 * Created by brandonyates on 4/21/16.
 */
public class OnJSONResponseCallback {

    JSONObject object;

    public void onJSONResponse(boolean success, JSONObject response) {

        if(success) {
            object = response;
        } else {
            object = null;
        }
    }

    public JSONObject getObject() { return object; }
}
