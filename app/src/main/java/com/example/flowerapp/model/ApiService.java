package com.example.flowerapp.model;

import com.example.flowerapp.model.data.Logout;
import com.example.flowerapp.model.response.GetBanner;
import com.example.flowerapp.model.response.GetCheckout;
import com.example.flowerapp.model.response.GetKategori;
import com.example.flowerapp.model.data.Login;
import com.example.flowerapp.model.response.GetKupon;
import com.example.flowerapp.model.response.GetProduk;
import com.example.flowerapp.model.response.GetRekening;
import com.example.flowerapp.model.response.PostCheckout;
import com.example.flowerapp.model.response.PostTransaksi;
import com.example.flowerapp.model.response.RegistUser;
import com.example.flowerapp.model.response.UpdateUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @GET("checkout/get-checkout")
    Call<GetCheckout> listCart();

    @GET("kupon/cek-kupon")
    Call<GetKupon> getKupon(@Query("kode") String kode);

    @GET("master-bank/get-bank")
    Call<GetRekening> getNorek();

    @FormUrlEncoded
    @POST("transaksi/store")
    Call<PostTransaksi> buatTransaksi(
            @Field("total_harga_transaksi") String totalHargaTransaksi,
            @Field("id_kupon") String idKupon,
            @Field("id_produk[]") String id_produk,
            @Field("jumlah[]") String jumlah,
            @Field("total_harga[]") String totalHarga
    );

    @POST("transaksi/store")
    Call<PostTransaksi> testTransaksi(
            @Body RequestTransaksi reqeust
    );
}
