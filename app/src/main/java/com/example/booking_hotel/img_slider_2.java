package com.example.booking_hotel;

public class img_slider_2 {
    private String imageUrl;
    private int resourceId;

    // Constructor for image URL
    public img_slider_2(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Constructor for resource ID
    public img_slider_2(int resourceId) {
        this.resourceId = resourceId;
    }

    // Getters
    public String getImageUrl() {
        return imageUrl;
    }

    public int getResourceId() {
        return resourceId;
    }
}
