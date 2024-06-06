package com.example.flowerapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.flowerapp.MainActivity;
import com.example.flowerapp.R;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.data.Kategori;
import com.example.flowerapp.model.response.GetKategori;
import com.example.flowerapp.ui.home.KategoriAdapter;
import com.example.flowerapp.util.SharedPrefManager;
import com.mrntlu.toastie.Toastie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private SharedPrefManager prefManager;
    private ApiService apiService;
    private final static String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prefManager = new SharedPrefManager(this);
        String token = prefManager.getToken();

        Log.d(TAG, "token: " + token);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(token != null){
                    apiService = RClient.getRetrofitInstanceWithAuth(token).create(ApiService.class);
                    apiService.getKategori().enqueue(new Callback<GetKategori>() {
                        @Override
                        public void onResponse(Call<GetKategori> call, Response<GetKategori> response) {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(Call<GetKategori> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getMessage());
                            prefManager.clearToken();
                            Toastie.topWarning(SplashActivity.this, "Session habis, mohon login kembali", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
                }else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 3000);
    }
}