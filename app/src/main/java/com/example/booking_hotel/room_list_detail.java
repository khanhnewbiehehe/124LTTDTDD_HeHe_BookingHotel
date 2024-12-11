package com.example.booking_hotel;

public class room_list_detail {

    private String roomName, roomCheckIn, roomCheckOut, roomTrangthai, roomID;

    public room_list_detail(String roomName, String roomCheckIn, String roomCheckOut, String roomTrangthai, String roomID) {
        this.roomName = roomName;
        this.roomCheckIn = roomCheckIn;
        this.roomCheckOut = roomCheckOut;
        this.roomTrangthai = roomTrangthai;
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomCheckIn() {
        return roomCheckIn;
    }

    public void setRoomCheckIn(String roomCheckIn) {
        this.roomCheckIn = roomCheckIn;
    }

    public String getRoomCheckOut() {
        return roomCheckOut;
    }

    public void setRoomCheckOut(String roomCheckOut) {
        this.roomCheckOut = roomCheckOut;
    }

    public String getRoomTrangthai() {
        return roomTrangthai;
    }

    public void setRoomTrangthai(String roomTrangthai) {
        this.roomTrangthai = roomTrangthai;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
}
