package com.example.androiddeveloper.faredriver;

/**
 * Created by Android Developer on 4/23/2018.
 */

public class Dataclass {
    String Name;
    String Address;
    String lat;

    public void setName(String name) {
        Name = name;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    String longi;

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getLat() {
        return lat;
    }

    public String getLongi() {
        return longi;
    }

    public Dataclass(String name, String address, String lat, String longi) {
        Name = name;
        Address = address;
        this.lat = lat;
        this.longi = longi;
    }
}
