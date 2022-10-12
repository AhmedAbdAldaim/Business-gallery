package com.example.businesgalleryadmin.Model;

import com.google.gson.annotations.SerializedName;


public class StatusOrdersModel {
    @SerializedName("work_id")
    private String work_id;
    @SerializedName("id")
    private String id;
    @SerializedName("work")
    private StatusOrdersModel2 statusOrderseModel2;

    public StatusOrdersModel2 getStatusOrderseModel2() {
        return statusOrderseModel2;
    }
    public void setStatusOrderseModel2(StatusOrdersModel2 statusOrderseModel2) {
        this.statusOrderseModel2 = statusOrderseModel2;
    }

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
