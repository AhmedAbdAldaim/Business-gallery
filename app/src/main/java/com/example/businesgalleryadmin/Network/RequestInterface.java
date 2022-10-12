package com.example.businesgalleryadmin.Network;

import com.example.businesgalleryadmin.Model.AddCommentResponse;
import com.example.businesgalleryadmin.Model.AddWorkResponse;
import com.example.businesgalleryadmin.Model.DeleteCommentResponse;
import com.example.businesgalleryadmin.Model.DeleteLoveResponse;
import com.example.businesgalleryadmin.Model.DeleteWorkResponse;
import com.example.businesgalleryadmin.Model.EditProfileResponse;
import com.example.businesgalleryadmin.Model.EditWorkResponse;
import com.example.businesgalleryadmin.Model.GetAllWorksByUserIDResponse;
import com.example.businesgalleryadmin.Model.LoginResponse;
import com.example.businesgalleryadmin.Model.SendLikeResponse;
import com.example.businesgalleryadmin.Model.LoveandCommentsResponse;
import com.example.businesgalleryadmin.Model.StatusOrdersResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RequestInterface {

    // <-- Login Api-->
    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> Login(
            @Field("email") String email,
            @Field("password") String password);

    // <-- Edit Profile Api-->
    @FormUrlEncoded
    @POST("profile")
    Call<EditProfileResponse> EditProfile(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Header("Authorization") String authorization);


    // <-- Edit Password Api -->
    @FormUrlEncoded
    @POST("profile")
    Call<EditProfileResponse> EditProfilePassword(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Header("Authorization") String authorization);
//  <----------------------------------Works --------------------------------------------->

    // <-- Add Work Api-->
    @Headers({"Accept: application/json"})
    @Multipart
    @POST("works")
    Call<AddWorkResponse> AddWork(
            @Part MultipartBody.Part name,
            @Part MultipartBody.Part details,
            @Part MultipartBody.Part price,
            @Part MultipartBody.Part file,
            @Header("Authorization") String authorization);

    // <-- Edit Work Api-->
    @Headers({"Accept: application/json"})
    @Multipart
    @POST("edit/works/{id}")
    Call<EditWorkResponse> EditWorks(
            @Path("id") String id,
            @Part MultipartBody.Part name,
            @Part MultipartBody.Part details,
            @Part MultipartBody.Part price,
            @Part MultipartBody.Part file,
            @Header("Authorization") String authorization);

    // <-- Get All Works By UserId Api -->
    @GET("user/{id}/works")
    Call<GetAllWorksByUserIDResponse> GetAllWorksByUserID(
            @Path("id") String id,
            @Header("Authorization") String authorization);

    // <-- Delete Works Api -->
    @DELETE("works/{id}")
    Call<DeleteWorkResponse> DeleteWorks(
            @Path("id") String id,
            @Header("Authorization") String authorization);

    //  <----------------------------------Likes --------------------------------------------->

    // <-- Send Like Api -->
    @FormUrlEncoded
    @POST("likes")
    Call<SendLikeResponse> SendLove(
            @Field("work_id") String work_id,
            @Header("Authorization") String authorization);

    // <-- Delete Like Api -->
    @DELETE("likes/{id}")
    Call<DeleteLoveResponse> DeleteLove(
            @Path("id") String id,
            @Header("Authorization") String authorization);


    // <-- GET Like Api -->
    @GET("works/{id}")
    Call<LoveandCommentsResponse> TotalLove(
            @Path("id") String id,
            @Header("Authorization") String authorization);

    //  <----------------------------------commenst --------------------------------------------->

    // <-- Send Comment Api -->
    @FormUrlEncoded
    @POST("comments")
    Call<AddCommentResponse> SendComment(
            @Field("work_id") String work_id,
            @Field("comment") String comment,
            @Header("Authorization") String authorization);

    // <-- GET Comments Api -->
    @GET("works/{id}")
    Call<LoveandCommentsResponse> GetallComments(
            @Path("id") String id,
            @Header("Authorization") String authorization);

    // <-- Delete Comments Api -->
    @DELETE("comments/{id}")
    Call<DeleteCommentResponse> DeleteComment(
            @Path("id") String id,
            @Header("Authorization") String authorization);

    // <!--- status Order -->
    @FormUrlEncoded
    @POST("status/orders/{id_order}")
    Call<StatusOrdersResponse> StatusOrder(
            @Path("id_order") String id_order,
            @Field("status") String status,
            @Header("Authorization") String authorization);

}


