package com.example.booking_hotel.model;

public class manager_roomlist_detail {
    private String maPhong;
    private String roomType;
    private String roomTrangthai;
    private String roomImage;

    // Constructor không tham số
    public manager_roomlist_detail() {
        // Firebase yêu cầu constructor không tham số
    }

    // Constructor có tham số (nếu cần)
    public manager_roomlist_detail(String maPhong, String roomType, String roomTrangthai, String roomImage) {
        this.maPhong = maPhong;
        this.roomType = roomType;
        this.roomTrangthai = roomTrangthai;
        this.roomImage = roomImage;
    }

    // Getters và Setters
    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
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

