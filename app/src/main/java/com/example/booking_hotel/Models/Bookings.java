package com.example.booking_hotel.Models;

public class Bookings {

    public String CheckIn, CheckOut, Id;
    public Integer Price;  // Kept as Integer
    public Integer RoomCode;
    public Integer RoomDiscount;  // Kept as Integer
    public String RoomID, Status;
    public Integer Total;  // Kept as Integer
    public String UserID, UserName, VoucherCode;
    public Integer VoucherDiscount;
    public String VoucherID;

    // Default constructor
    public Bookings() {

    }

    // Constructor with Integer data types for price, discount, and total
    public Bookings(String checkIn, String checkOut, String id, Integer price, Integer roomCode, Integer roomDiscount, String roomID, String status, Integer total, String userID, String userName, String voucherCode, Integer voucherDiscount, String voucherID) {
        CheckIn = checkIn;
        CheckOut = checkOut;
        Id = id;
        Price = price;
        RoomCode = roomCode;
        RoomDiscount = roomDiscount;
        RoomID = roomID;
        Status = status;
        Total = total;
        UserID = userID;
        UserName = userName;
        VoucherCode = voucherCode;
        VoucherDiscount = voucherDiscount;
        VoucherID = voucherID;
    }

    // Getters and Setters
    public String getCheckIn() {
        return CheckIn;
    }

    public void setCheckIn(String checkIn) {
        CheckIn = checkIn;
    }

    public String getCheckOut() {
        return CheckOut;
    }

    public void setCheckOut(String checkOut) {
        CheckOut = checkOut;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer price) {
        Price = price;
    }

    public Integer getRoomCode() {
        return RoomCode;
    }

    public void setRoomCode(Integer roomCode) {
        RoomCode = roomCode;
    }

    public Integer getRoomDiscount() {
        return RoomDiscount;
    }

    public void setRoomDiscount(Integer roomDiscount) {
        RoomDiscount = roomDiscount;
    }

    public String getRoomID() {
        return RoomID;
    }

    public void setRoomID(String roomID) {
        RoomID = roomID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Integer getTotal() {
        return Total;
    }

    public void setTotal(Integer total) {
        Total = total;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getVoucherCode() {
        return VoucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        VoucherCode = voucherCode;
    }

    public Integer getVoucherDiscount() {
        return VoucherDiscount;
    }

    public void setVoucherDiscount(Integer voucherDiscount) {
        VoucherDiscount = voucherDiscount;
    }

    public String getVoucherID() {
        return VoucherID;
    }

    public void setVoucherID(String voucherID) {
        VoucherID = voucherID;
    }
}
