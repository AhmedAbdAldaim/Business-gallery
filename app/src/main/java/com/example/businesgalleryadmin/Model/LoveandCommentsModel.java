package com.example.businesgalleryadmin.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoveandCommentsModel {
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
    @SerializedName("likes")
    private List<LoveandCommentsModel2> loveandCommentsModel;
    @SerializedName("comments")
    private List<LoveandCommentsModel2> commentsModel;

    public List<LoveandCommentsModel2> getLoveandCommentsModel() {
        return loveandCommentsModel;
    }
    public void setLoveandCommentsModel(List<LoveandCommentsModel2> loveandCommentsModel) {
        this.loveandCommentsModel = loveandCommentsModel;
    }

    public List<LoveandCommentsModel2> getCommentsModel() {
        return commentsModel;
    }
    public void setCommentsModel(List<LoveandCommentsModel2> commentsModel) {
        this.commentsModel = commentsModel;
    }

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