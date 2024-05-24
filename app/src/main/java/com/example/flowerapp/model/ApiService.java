package com.example.flowerapp.model;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Ardian Iqbal Yusmartito on 23/05/24
 * Github : https://github.com/ALU-syntax
 * Twitter : https://twitter.com/mengkerebe
 * Instagram : https://www.instagram.com/ardian_iqbal_
 * LinkedIn : https://www.linkedin.com/in/ardianiqbal
 */
public interface ApiService {
    @FormUrlEncoded
    @POST("login")
    Call<GetLogin> login(
            @Field("email") String email,
            @Field("password") String password
    );

}
