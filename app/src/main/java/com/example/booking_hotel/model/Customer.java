package com.example.booking_hotel.model;

public class Customer {
    private String cusPhone;
    private String cusFullname;
    private String cusPass;
    private String cusEmail;


    public Customer() {
    }

    public Customer(String cusPhone, String cusFullname , String cusPass , String cusEmail) {
        this.cusPhone = cusPhone;
        this.cusFullname = cusFullname;
        this.cusPass = cusPass;
        this.cusEmail = cusEmail;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    public String getCusFullname() {
        return cusFullname;
    }

    public void setCusFullname(String cusFullname) {
        this.cusFullname = cusFullname;
    }

    public String getCusPass() {
        return cusPass;
    }

    public void setCusPass(String cusPass) {
        this.cusPass = cusPass;
    }

    public String getCusEmail() {
        return cusEmail;
    }

    public void setCusEmail(String cusEmail) {
        this.cusEmail = cusEmail;
    }
}
