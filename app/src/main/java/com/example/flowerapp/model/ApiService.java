package com.example.flowerapp.model;

import com.example.flowerapp.model.data.Logout;
import com.example.flowerapp.model.response.GetBanner;
import com.example.flowerapp.model.response.GetKategori;
import com.example.flowerapp.model.data.Login;
import com.example.flowerapp.model.response.GetProduk;
import com.example.flowerapp.model.response.PostCheckout;
import com.example.flowerapp.model.response.RegistUser;
import com.example.flowerapp.model.response.UpdateUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

    @GET("master-banner/get-banner")
    Call<GetBanner> getBanner();

    @GET("kategori/get-kategori")
    Call<GetKategori> getKategori();

    @GET("produk/get-produk")
    Call<GetProduk> getProduk();

    @FormUrlEncoded
    @POST("users/update")
    Call<UpdateUser> updateUser(
            @Field("nama") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("logout")
    Call<Logout> logout();

    @FormUrlEncoded
    @POST("checkout/store")
    Call<PostCheckout> checkout(
            @Field("id_produk") String idProduk,
            @Field("jumlah") String jumlah
    );

}
