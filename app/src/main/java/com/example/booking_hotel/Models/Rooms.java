package com.example.booking_hotel.Models;

import java.util.List;

public class Rooms {

   public String AreaName, Code, Id, Status, TypeName;
   public List<String> Images;
   public Double Discount, Price;

   public Rooms() {

   }

   public Rooms(String areaName, String code, double discount, String id, List<String> images,double price, String status, String typeName) {
      AreaName = areaName;
      Code = code;
      Id = id;
      Status = status;
      TypeName = typeName;
      Images = images;
      Discount = discount;
      Price = price;
   }

   public String getAreaName() {
      return AreaName;
   }

   public void setAreaName(String areaName) {
      AreaName = areaName;
   }

   public String getCode() {
      return Code;
   }

   public void setCode(String code) {
      Code = code;
   }

   public String getId() {
      return Id;
   }

   public void setId(String id) {
      Id = id;
   }

   public String getStatus() {
      return Status;
   }

   public void setStatus(String status) {
      Status = status;
   }

   public String getTypeName() {
      return TypeName;
   }

   public void setTypeName(String typeName) {
      TypeName = typeName;
   }

   public List<String> getImages() {
      return Images;
   }

   public void setImages(List<String> images) {
      Images = images;
   }

   public double getDiscount() {
      return Discount;
   }

   public void setDiscount(double discount) {
      Discount = discount;
   }

   public double getPrice() {
      return Price;
   }

   public void setPrice(double price) {
      Price = price;
   }
}
