package com.example.flowerapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.flowerapp.databinding.ActivityLoginBinding;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.GetLogin;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.util.SharedPrefManager;
import com.mrntlu.toastie.Toastie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private ApiService apiService;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        retrofit = RClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(binding.etEmail.toString(), binding.etPassword.toString());
            }
        });

        binding.txtRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    public void login(String email, String password){
        Call<GetLogin> call = apiService.login(email, password);
        Log.d("Login Activity", "login: " + email);
        Log.d("Login Activity", "login: " + email);
        call.enqueue(new Callback<GetLogin>() {
            @Override
            public void onResponse(Call<GetLogin> call, Response<GetLogin> response) {

                if(response.isSuccessful()){

                    GetLogin loginResponse = response.body();
                    if(loginResponse != null && loginResponse.isSuccess()){
                        Toastie.topSuccess(LoginActivity.this, "Login Berhasil", Toast.LENGTH_LONG).show();
                        String token = loginResponse.getToken();

                        // Simpan token ke SharedPreferences
                        SharedPrefManager.getInstance(LoginActivity.this).saveToken(token);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toastie.topWarning(LoginActivity.this, "Login Gagal, Password atau email salah", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetLogin> call, Throwable t) {
                Toastie.topWarning(LoginActivity.this, "Error," + t.getMessage().toString(), Toast.LENGTH_LONG).show();
                Log.d("LoginActivity", "onFailure: "+ t.getMessage());
            }
        });
    }
}