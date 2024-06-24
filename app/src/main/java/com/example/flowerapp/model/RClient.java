package com.example.flowerapp.model;

import com.example.flowerapp.util.SharedPrefManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RClient {
    private static Retrofit retrofit;
    private static  String BASE_URL = "http://192.168.1.5:8000/api/";
//    private static  String BASE_URL = "https://neidra.my.id/api/";

    public static String BASE_URL_IMAGE = "http://192.168.1.5:8000/";
//public static String BASE_URL_IMAGE = "https://neidra.my.id/";
    public static String getBaseUrl() {
        return BASE_URL_IMAGE;
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitInstanceWithAuth(String token){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(token))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
