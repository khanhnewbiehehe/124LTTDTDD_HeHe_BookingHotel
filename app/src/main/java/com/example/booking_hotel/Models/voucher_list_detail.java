package com.example.booking_hotel.Models;

public class voucher_list_detail {

    private String voucherName, voucherDescription, voucherDiscount, voucherImage, voucherCode;

    public voucher_list_detail(String voucherName, String voucherDescription, String voucherDiscount, String voucherImage, String voucherCode) {
        this.voucherName = voucherName;
        this.voucherDescription = voucherDescription;
        this.voucherDiscount = voucherDiscount;
        this.voucherImage = voucherImage;
        this.voucherCode = voucherCode;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getVoucherDescription() {
        return voucherDescription;
    }

    public void setVoucherDescription(String voucherDescription) {
        this.voucherDescription = voucherDescription;
    }

    public String getVoucherDiscount() {
        return voucherDiscount;
    }

    public void setVoucherDiscount(String voucherDiscount) {
        this.voucherDiscount = voucherDiscount;
    }

    public String getVoucherImage() {
        return voucherImage;
    }

    public void setVoucherImage(String voucherImage) {
        this.voucherImage = voucherImage;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }
}
