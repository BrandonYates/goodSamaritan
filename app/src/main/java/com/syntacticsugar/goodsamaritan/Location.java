package com.syntacticsugar.goodsamaritan;

/**
 * Created by brandonyates on 4/21/16.
 */
public class Location {
    private String id; // id for mongo
    private double latitude; // latitude of location
    private double longitude; // longitude of location

    public Location () {}

    public Location (String id, double latitude, double longitude) { //constructor
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() { return id; } // get id

    public void setId(String id) { this.id = id; } // set id

    public double getLatitude() { return latitude; } //  returns the latitude

    public void setLatitude(long latitude) { this.latitude = latitude; } // sets the latitude

    public double getLongitude() { return longitude; } // returns the longitude

    public void setLongitude(long longitude) { this.longitude = longitude; } // sets the longitude

    public String toString() { // send id , lat and long to a string value
        return "Id: " + this.id + "\nLatitude: " + this.latitude + "\nLongitude: " + this.longitude ;
    }

    public boolean equals(Location candidate) {

        boolean idMatch = (this.id.equals(candidate.id)); // see if the 2 ids are a match
        boolean latMatch = (this.latitude == candidate.latitude); // see if the latitude is a match
        boolean longMatch = (this.longitude == candidate.longitude); // see if the longitude is a match

        return (idMatch && latMatch && longMatch); // return the value true if all 3 match, false otherwise
    }

    public void copy(Location assignee) { //copy function
        this.id = assignee.id;
        this.latitude = assignee.latitude;
        this.longitude = assignee.longitude;
    }
}
