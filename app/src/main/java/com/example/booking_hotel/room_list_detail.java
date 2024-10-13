package com.example.booking_hotel;

public class room_list_detail {

    private String roomName, roomType, roomDate, roomTrangthai, roomImage;

    public room_list_detail(String roomName, String roomType, String roomDate, String roomTrangthai, String roomImage) {
        this.roomName = roomName;
        this.roomType = roomType;
        this.roomDate = roomDate;
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

    public String getRoomDate() {
        return roomDate;
    }

    public void setRoomDate(String roomDate) {
        this.roomDate = roomDate;
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
