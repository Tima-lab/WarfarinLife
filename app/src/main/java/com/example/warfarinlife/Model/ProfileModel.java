package com.example.warfarinlife.Model;

public class ProfileModel {

    private int id;
    private String name;
    private double mno;
    private String date;
    private String time;

    public ProfileModel(int id, String name, double mno ,String data, String time) {
        this.id = id;
        this.name = name;
        this.mno = mno;
        this.date = data;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMno() {
        return mno;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}