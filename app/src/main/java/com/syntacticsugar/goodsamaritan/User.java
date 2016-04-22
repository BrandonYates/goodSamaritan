package com.syntacticsugar.goodsamaritan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

/**
 * Created by brandonyates on 4/21/16.
 */
public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private Collection<Deed> deeds;
    private int points;

    public User () {}

    public User (String id, String firstName, String lastName, String emailAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.deeds = null;
    }

    //
    public User (JSONObject object) throws JSONException{

        JSONObject rawUser = (JSONObject)object.get("entity");

        System.out.print("raw: " + rawUser.toString());

        String id = (String)rawUser.get("id");
        String fn = (String)rawUser.get("firstName");
        String ln = (String)rawUser.get("lastName");
        String email = (String)rawUser.get("emailAddress");

        JSONArray rawDeeds = (JSONArray)rawUser.get("deeds");


        int points = 0;
        for (int i = 0; i < rawDeeds.length(); i++) {

            Deed deed = new Deed(rawDeeds.getJSONObject(i));
            this.deeds.add(deed);
            points += deed.getPointValue();
        }
    }


}
