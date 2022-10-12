package com.example.businesgalleryadmin.Model;

import com.google.gson.annotations.SerializedName;

public class SendLikeModel {
    @SerializedName("work_id")
    private String work_id;
    @SerializedName("id")
    private String id;

    public String getWork_id() {
        return work_id;
    }
    public void setWork_id(String work_id) {
        this.work_id = work_id;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}