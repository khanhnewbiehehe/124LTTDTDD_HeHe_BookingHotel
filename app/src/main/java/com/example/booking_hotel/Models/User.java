package com.example.booking_hotel.Models;

public class User {
    private String AvatarUrl;
    private String Email;
    private String Id;
    private String Name;
    private String Password;
    private String PhoneNumber;
    private String Role;
    private int Vip;

    public User() {
    }

    public User(String avatarUrl, String email, String id, String name, String password, String phoneNumber, String role, int vip) {
        AvatarUrl = avatarUrl;
        Email = email;
        Id = id;
        Name = name;
        Password = password;
        PhoneNumber = phoneNumber;
        Role = role;
        Vip = vip;
    }

    public String getAvatarUrl() {
        return AvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        AvatarUrl = avatarUrl;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public int getVip() {
        return Vip;
    }

    public void setVip(int vip) {
        Vip = vip;
    }
}
