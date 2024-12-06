package com.example.booking_hotel.Models;

public class HotelArea {

    public String Id;
    public String Name;

    public HotelArea() {

    }

    public HotelArea(String id, String name) {
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
