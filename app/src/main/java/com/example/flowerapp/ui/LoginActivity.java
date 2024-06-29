package com.example.flowerapp.ui;

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
import androidx.fragment.app.FragmentManager;

import com.devhoony.lottieproegressdialog.LottieProgressDialog;
import com.example.flowerapp.MainActivity;
import com.example.flowerapp.R;
import com.example.flowerapp.databinding.ActivityLoginBinding;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.User;
import com.example.flowerapp.model.data.Login;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.util.SharedPrefManager;
import com.mrntlu.toastie.Toastie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private static String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private ApiService apiService;
    private Retrofit retrofit;
    private LottieProgressDialog lottieProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        retrofit = RClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);
        lottieProgressDialog = new LottieProgressDialog(this,
                false, null, null, null,
                null, LottieProgressDialog.SAMPLE_6, null, null);
        EdgeToEdge.enable(this);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.etEmail.getText().toString().isEmpty()){
                    Toastie.topWarning(LoginActivity.this, "Email Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                } else if (binding.etPassword.getText().toString().isEmpty()) {
                    Toastie.topWarning(LoginActivity.this, "Password Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                }else{
                    login(binding.etEmail.getText().toString(), binding.etPassword.getText().toString());
                }

            }
        });

        binding.txtRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        binding.tvResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login(String email, String password){
        FragmentManager fragmentManager = getSupportFragmentManager();
//        loadingDialog.show(fragmentManager, "loading");
        lottieProgressDialog.show();
        Call<Login> call = apiService.login(email, password);
        Log.d(TAG, "email: " + email);
        Log.d(TAG, "passowrd: " + password);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login loginResponse = response.body();
                User user = loginResponse.getUser();
                Log.d(TAG, "onResponse: " + loginResponse);
                if(response.isSuccessful()){

                    if(loginResponse != null && loginResponse.isSuccess()){
                        Toastie.topSuccess(LoginActivity.this, "Login Berhasil", Toast.LENGTH_LONG).show();

                        String token = loginResponse.getToken();
                        // Simpan token ke SharedPreferences
                        SharedPrefManager.getInstance(LoginActivity.this).saveToken(token);

                        // simpan data user ke sharedpreferencecs
                        SharedPrefManager.getInstance(LoginActivity.this).saveUser(user);
//                        loadingDialog.dismiss();
                        lottieProgressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toastie.topWarning(LoginActivity.this, "Login Gagal, Password atau email salah", Toast.LENGTH_LONG).show();
//                        loadingDialog.dismiss();
                        lottieProgressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toastie.topWarning(LoginActivity.this, "Error," + t.getMessage().toString(), Toast.LENGTH_LONG).show();
                Log.d("LoginActivity", "onFailure: "+ t.getMessage());
//                loadingDialog.dismiss();
                lottieProgressDialog.dismiss();
            }

        });
    }
}