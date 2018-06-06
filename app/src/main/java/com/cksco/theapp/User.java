package com.cksco.theapp;

import java.util.ArrayList;

public class User {
    public String name;
    public String id;
    public int amount;
    public ArrayList<customLatLng> hits = new ArrayList<>();
    public String school;

    public User() {

    }

    public User(String name, String id) {
        this.name = name;
        this.id = id;
        amount = 0;
    }

    public User(String name, String school, String id) {
        this.name = name;
        this.id = id;
        this.amount = 0;
        this.school = new String(school);
    }

    public User(String name, String school, String id, int amount) {
        this.name = name;
        this.id = id;
        this.amount = amount;
        this.school = new String(school);
    }

    public User(String name, String school, String id, ArrayList<customLatLng> hits) {
        this.name = name;
        this.id = id;
        amount = 0;
        this.hits = hits;
        this.school = new String(school);
    }

    public User(String name, String school, String id, int amount, ArrayList<customLatLng> hits) {
        this.name = name;
        this.id = id;
        this.amount = amount;
        this.hits = hits;
        this.school = new String(school);

    }
}
