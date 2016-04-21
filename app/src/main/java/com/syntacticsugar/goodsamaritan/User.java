package com.syntacticsugar.goodsamaritan;

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
    public User (JSONObject object) {


    }


}
