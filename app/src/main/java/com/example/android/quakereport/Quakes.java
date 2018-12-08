package com.example.android.quakereport;

public class Quakes {

    private double magnitude;
    private String primaryLocation, locationOffset;
    private String date, time, quakeUrl;

    public Quakes(double magnitude, String primaryLocation, String offsetLocation
            , String date, String time, String quakeUrl) {
        this.magnitude = magnitude;
        this.primaryLocation = primaryLocation;
        this.locationOffset = offsetLocation;
        this.date = date;
        this.time = time;
        this.quakeUrl = quakeUrl;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getPrimaryLocation() {
        return primaryLocation;
    }

    public void setPrimaryLocation(String primaryLocation) {
        this.primaryLocation = primaryLocation;
    }

    public String getLocationOffset() {
        return locationOffset;
    }

    public void setLocationOffset(String locationOffset) {
        this.locationOffset = locationOffset;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQuakeUrl() {
        return quakeUrl;
    }

    public void setQuakeUrl(String quakeUrl) {
        this.quakeUrl = quakeUrl;
    }
}
