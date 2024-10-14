package com.example.booking_hotel;

public class manager_roomlist_detail {

    private String roomName, roomType, roomTrangthai, roomImage;

    public manager_roomlist_detail(String roomName, String roomType, String roomTrangthai, String roomImage) {
        this.roomName = roomName;
        this.roomType = roomType;
        this.roomTrangthai = roomTrangthai;
        this.roomImage = roomImage;
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

    public String getRoomTrangthai() {
        return roomTrangthai;
    }

    public void setRoomTrangthai(String roomTrangthai) {
        this.roomTrangthai = roomTrangthai;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }
}
