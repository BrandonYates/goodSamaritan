package com.syntacticsugar.goodsamaritan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by brandonyates on 4/21/16.
 */
public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private ArrayList<Deed> deeds;
    private int points;

    public User () {}

    public User (String id, String firstName, String lastName, String emailAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.deeds = new ArrayList<Deed>();
    }

    //
    public User (JSONObject rawUser) throws JSONException{

        System.out.print("raw: " + rawUser.toString());

        this.id = rawUser.getString("id");
        this.firstName = rawUser.getString("firstName");
        this.lastName = rawUser.getString("lastName");
        this.emailAddress = rawUser.getString("emailAddress");

        this.points = 0;
        this.deeds = new ArrayList<Deed>();
        try {
            JSONArray rawDeeds = rawUser.getJSONArray("deeds");


            for (int i = 0; i < rawDeeds.length(); i++) {

                Deed deed = new Deed(rawDeeds.getJSONObject(i));
                this.deeds.add(deed);
                this.points += deed.getPointValue();
            }
        } catch (JSONException e) {
            System.out.println("deeds was null");
        }
    }

    public ArrayList<Deed> getDeeds() { return this.deeds; }

    public void setDeeds(ArrayList<Deed> deeds) { this.deeds = deeds;}

    public String getFirstName() { return this.firstName; }

    public String getLastName() { return this.lastName; }

    public String getEmailAddress() { return this.emailAddress; };

    public Integer getPoints() { return this.points; }
}
