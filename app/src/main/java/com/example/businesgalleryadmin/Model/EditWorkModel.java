package com.example.businesgalleryadmin.Model;

import com.google.gson.annotations.SerializedName;

public class EditWorkModel {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("details")
    private String details;
    @SerializedName("price")
    private String price;
    @SerializedName("photo")
    private String photo;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
}