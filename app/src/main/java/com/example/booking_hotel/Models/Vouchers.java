package com.example.booking_hotel.Models;

public class Vouchers {

    public String Code, Description, Id, ImageUrl, Name, Status;
    public Double Discount;
    public int Quantity;

    public Vouchers() {

    }

    public Vouchers(String code, String description, String id, String imageUrl, String name, String status, double discount, int quantity) {
        Code = code;
        Description = description;
        Id = id;
        ImageUrl = imageUrl;
        Name = name;
        Status = status;
        Discount = discount;
        Quantity = quantity;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
