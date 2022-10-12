package com.example.businesgalleryadmin.Model;

import com.google.gson.annotations.SerializedName;

public class AddCommentModel {
    @SerializedName("id")
    private String id;
    @SerializedName("comment")
    private String comment;
    @SerializedName("work_id")
    private String work_id;
    @SerializedName("user_id")
    private String user_id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getWork_id() {
        return work_id;
    }
    public void setWork_id(String work_id) {
        this.work_id = work_id;
    }

    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}