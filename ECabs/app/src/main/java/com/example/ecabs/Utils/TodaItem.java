package com.example.ecabs.Utils;

public class TodaItem {
    private String todaName;
    private String todaLoc;
    private double todaFare;

    // Constructors
    public TodaItem(String todaName, int anInt, double aDouble) {
        // Default constructor
    }

    public TodaItem(String todaName, String todaLoc, double todaFare) {
        this.todaName = todaName;
        this.todaLoc = todaLoc;
        this.todaFare = todaFare;
    }

    // Getters and Setters
    public String getTodaName() {

        return todaName;
    }

    public void setTodaName(String todaName) {

        this.todaName = todaName;
    }

    public String getTodaLoc() {
        return todaLoc;
    }

    public void setTodaLoc(String todaLoc) {

        this.todaLoc = todaLoc;
    }

    public double getTodaFare() {
        return todaFare;
    }

    public void setTodaFare(double todaFare) {
        this.todaFare = todaFare;
    }
}
