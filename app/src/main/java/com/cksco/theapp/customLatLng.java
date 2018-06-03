package com.cksco.theapp;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class customLatLng {
    double latitude;
    double longitude;
    private MarkerOptions marker;
    int amount;
    long id;
    String user;
    long time;

    public customLatLng() {
    }

    public customLatLng(double lat, double lng, long _id, int amt, String user, long time) {
        latitude = lat;
        longitude = lng;
        amount = amt;
        marker = new MarkerOptions().position(this.toLatLng()).title(amount + " Detections Here");
        id = _id;
        this.user = user;
        this.time = time;
    }

    public customLatLng(double lat, double lng, long _id, long amt, String user, long time) {
        latitude = lat;
        longitude = lng;
        amount = (int) amt;
        marker = new MarkerOptions().position(this.toLatLng()).title(amount + " Detections Here");
        id = _id;
        this.user = user;
        this.time = time;
    }

    public customLatLng(LatLng l, long _id, int amt, String user, long time) {
        latitude = l.latitude;
        longitude = l.longitude;
        amount = amt;
        marker = new MarkerOptions().position(this.toLatLng()).title(amount + " Detections Here");
        id = _id;
        this.user = user;
        this.time = time;
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }

    public MarkerOptions getMarkerOptions() {
        return marker;
    }

    public void increaseAmount(DatabaseReference ref) {
        amount++;
        marker.title(amount + " Detections Here");
    }
}
