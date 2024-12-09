package com.example.booking_hotel.Models;

public class RoomType {

    public String Id;
    public String Name;
    public String Description;
    public String ImageUrl;
    public int People;

    public RoomType() {

    }

    public RoomType(String id, String name, String description, String imageUrl, int people) {
        Id = id;
        Name = name;
        Description = description;
        ImageUrl = imageUrl;
        People = people;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public int getPeople() {
        return People;
    }

    public void setPeople(int people) {
        People = people;
    }
}
