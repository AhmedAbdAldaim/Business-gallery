package com.example.businesgalleryadmin.Model;

import com.google.gson.annotations.SerializedName;


public class GetAllOrdersModel {
    @SerializedName("status")
    private String status;
    @SerializedName("id")
    private String order_id;
    @SerializedName("name")
    private String name_of_theorder;
    @SerializedName("bio")
    private String phone;

    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private String price;
    @SerializedName("photo")
    private String photo;


    public GetAllOrdersModel(String order_id, String status,String name_of_theorder,String phone, String name, String price, String photo) {
        this.order_id = order_id;
        this.status = status;
        this.name_of_theorder = name_of_theorder;
        this.phone=phone;
        this.name = name;
        this.price = price;
        this.photo = photo;
    }

    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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

    public String getName_of_theorder() {
        return name_of_theorder;
    }
    public void setName_of_theorder(String name_of_theorder) {
        this.name_of_theorder = name_of_theorder;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
