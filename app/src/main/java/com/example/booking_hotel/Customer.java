package com.example.booking_hotel;

public class Customer {
    private String CusID; // Phone number
    private String CusFullname;
    private String CusEmail;
    private String CusPassword;

    public Customer(String CusID,String CusFullname){
        this.CusID = CusID;
        this.CusFullname = CusFullname;
        this.CusEmail = "";
        this.CusPassword = "";
    }

    public Customer(String CusID,String CusFullname,String CusEmail,String CusPassword){
        this.CusID = CusID;
        this.CusFullname = CusFullname;
        this.CusEmail = CusEmail;
        this.CusPassword = CusPassword;
    }

    public String getCusFullname(){
        return CusFullname;
    }

    public void setCusFullname(String CusFullname){
        this.CusFullname = CusFullname;
    }

    public String getCusID(){
        return CusID;
    }

    public void setCusID(String CusID){
        this.CusID = CusID;
    }

    public String getEmail() {
        return CusEmail;
    }

    public void setCusEmail(String CusEmail){
        this.CusEmail = CusEmail;
    }

    public String getCusPassword(){
        return CusPassword;
    }

    public void setCusPassword(String CusPassword){
        this.CusPassword = CusPassword;
    }

}
