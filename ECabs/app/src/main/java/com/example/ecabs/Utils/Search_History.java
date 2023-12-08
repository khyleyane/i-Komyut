package com.example.ecabs.Utils;

public class Search_History {

    private String id;
    private String location;
    private String destination;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    private String Date;

    // Default constructor required for Firebase
    public Search_History() {
    }

    public Search_History(String id, String location, String destination) {
        this.id = id;
        this.location = location;
        this.destination = destination;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
