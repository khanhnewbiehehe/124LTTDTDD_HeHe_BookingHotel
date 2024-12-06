package com.example.booking_hotel.Models;

public class User {

    public String Id;
    public String Name;
    public String Email;
    public String PhoneNumber;
    public String Password;
    public String AvatarUrl;
    public String Role;
    public int Vip;

    public User() {

    }

    public User(String id, String name, String email, String phoneNumber, String password, String avatarUrl, String role, int vip) {
        Id = id;
        Name = name;
        Email = email;
        PhoneNumber = phoneNumber;
        Password = password;
        AvatarUrl = avatarUrl;
        Role = role;
        Vip = vip;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAvatarUrl() {
        return AvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        AvatarUrl = avatarUrl;
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
