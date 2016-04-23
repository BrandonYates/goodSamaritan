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

        this.id = rawDeed.getString("id");
        this.description = rawDeed.getString("description");
        this.date = new Date(rawDeed.getString("date"));
        this.requestingUserId = rawDeed.getString("requestingUserId");
        this.location = new Location(rawDeed.getJSONObject("location"));
        this.active = rawDeed.getBoolean("active");
        this.pointValue = rawDeed.getInt("pointValue");
    }

    public int getPointValue() { return pointValue; }

    public void setPointValue(int pointValue) { this.pointValue = pointValue; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getRequestingUserId() { return requestingUserId; }

    public void setRequestingUserId(String requestingUserId) { this.requestingUserId = requestingUserId; }

    public Set<String> getClaimedUserIds() { return claimedUserIds; }

    public void setClaimedUserIds(Set<String> claimedUserIds) { this.claimedUserIds = claimedUserIds; }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }

    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }
}
