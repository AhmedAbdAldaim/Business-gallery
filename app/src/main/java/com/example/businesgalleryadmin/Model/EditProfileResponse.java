package com.example.businesgalleryadmin.Model;
import com.google.gson.annotations.SerializedName;

public class EditProfileResponse {
    @SerializedName("user")
    private EditProfileModel editprofileModel;
    @SerializedName("error")
    private boolean error;
    @SerializedName("message_en")
    private String message_en;
    @SerializedName("message_ar")
    private String message_ar;


    public EditProfileModel getEditprofileModel() {
        return editprofileModel;
    }
    public void setEditprofileModel(EditProfileModel editprofileModel) {
        this.editprofileModel = editprofileModel;
    }

    public boolean isError() {
        return error;
    }
    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage_en() {
        return message_en;
    }
    public void setMessage_en(String message_en) {
        this.message_en = message_en;
    }

    public String getMessage_ar() {
        return message_ar;
    }
    public void setMessage_ar(String message_ar) {
        this.message_ar = message_ar;
    }
}
