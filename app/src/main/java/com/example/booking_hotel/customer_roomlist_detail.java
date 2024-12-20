package com.example.booking_hotel;

public class customer_roomlist_detail {

    private String roomName, roomType, roomPrice, roomImage, roomID;

    public customer_roomlist_detail(String roomName, String roomType, String roomPrice, String roomImage, String roomID) {
        this.roomName = roomName;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.roomImage = roomImage;
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
}
