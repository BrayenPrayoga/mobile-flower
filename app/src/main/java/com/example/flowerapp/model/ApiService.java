package com.example.flowerapp.model;

import com.example.flowerapp.model.response.Login;
import com.example.flowerapp.model.response.RegistUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("login")
    Call<Login> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("users/store")
    Call<RegistUser> registUser(
            @Field("nama") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("role") String role
    );

}
