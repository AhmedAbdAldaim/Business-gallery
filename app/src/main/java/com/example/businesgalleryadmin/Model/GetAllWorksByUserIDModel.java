package com.example.businesgalleryadmin.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllWorksByUserIDModel {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("role")
    private String role;
    @SerializedName("works")
    private List<GetAllWorksByUserIDModel2> getAllWorksByUserIDModel2;


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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public List<GetAllWorksByUserIDModel2> getGetAllWorksByUserIDModel2() {
        return getAllWorksByUserIDModel2;
    }
    public void setGetAllWorksByUserIDModel2(List<GetAllWorksByUserIDModel2> getAllWorksByUserIDModel2) {
        this.getAllWorksByUserIDModel2 = getAllWorksByUserIDModel2;
    }
}
