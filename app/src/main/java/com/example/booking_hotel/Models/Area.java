package com.example.booking_hotel.Models;

public class Area {

    public String Id;
    public String Name;

    public Area() {

    }

    public Area(String id, String name) {
        Id = id;
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
