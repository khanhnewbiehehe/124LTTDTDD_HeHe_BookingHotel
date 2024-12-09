package com.example.booking_hotel.Models;
import java.time.LocalDateTime;
public class Bookings {

    public LocalDateTime CheckIn, CheckOut;
    public String Id, RoomCode, RoomID, Status, UserID, UserName;
    public Double Price, Total;

    public Bookings() {

    }

    public Bookings(LocalDateTime checkIn, LocalDateTime checkOut, String id, String roomCode, String roomID, String status, String userID, String userName, double price, double total) {
        CheckIn = checkIn;
        CheckOut = checkOut;
        Id = id;
        RoomCode = roomCode;
        RoomID = roomID;
        Status = status;
        UserID = userID;
        UserName = userName;
        Price = price;
        Total = total;
    }

    public LocalDateTime getCheckIn() {
        return CheckIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        CheckIn = checkIn;
    }

    public LocalDateTime getCheckOut() {
        return CheckOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        CheckOut = checkOut;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getRoomCode() {
        return RoomCode;
    }

    public void setRoomCode(String roomCode) {
        RoomCode = roomCode;
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

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }
}
