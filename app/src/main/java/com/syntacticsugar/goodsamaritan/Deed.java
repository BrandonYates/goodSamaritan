package com.syntacticsugar.goodsamaritan;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by brandonyates on 4/21/16.
 */
public class Deed {
    private String id;
    private String description;
    private Date date;
    private String  requestingUserId;
    private Set<String> claimedUserIds = Collections.synchronizedSet(new HashSet<String>());
    private Location location = new Location();
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private boolean active;

    private int pointValue;

    public Deed() {};

    public Deed(String id, String desc, long t, String uid, Location l, int points) {
        this.id = id;
        this.description = desc;
        this.date = new Date(t);
        this.requestingUserId = uid;
        this.location.copy(l);
        this.pointValue = points;
    };

//    public Deed(String id, String desc, String d, String uid, Location l, int points) {
//        this.id = id;
//        this.description = desc;
//        try {
//            this.date = df.parse(d);
//        } catch(ParseException ex) {
//            System.out.println(ex.getMessage());
//        }
//        this.requestingUserId = uid;
//        this.location.copy(l);
//        this.claimedUserIds = Collections.synchronizedSet(new HashSet<String>());
//
//        this.pointValue = points;
//    };

    public Deed(JSONObject rawDeed) throws JSONException{

        this.id = (String)rawDeed.get("id");
        this.description = (String)rawDeed.get("description");
        this.date = (Date)rawDeed.get("date");
        this.requestingUserId = (String)rawDeed.get("requestingUserId");
        this.location = new Location((JSONObject)rawDeed.get("location"));
        this.active = (boolean)rawDeed.get("active");
        this.pointValue = (int)rawDeed.get("pointValue");
    }

    public int getPointValue() { return pointValue; }

    public void setPointValue(int pointValue) { this.pointValue = pointValue; }

}
