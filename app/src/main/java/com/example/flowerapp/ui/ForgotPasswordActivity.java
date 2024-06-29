package com.example.flowerapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devhoony.lottieproegressdialog.LottieProgressDialog;
import com.example.flowerapp.R;
import com.example.flowerapp.databinding.ActivityForgotPasswordBinding;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.RClient;
import com.mrntlu.toastie.Toastie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private Retrofit retrofit;
    private ApiService apiService;
    private LottieProgressDialog lottieProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        retrofit = RClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);
        lottieProgressDialog = new LottieProgressDialog(this,
                false, null, null, null,
                null, LottieProgressDialog.SAMPLE_6, null, null);

        binding.btnConfirmReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lottieProgressDialog.show();
                if(binding.etEmail.getText().toString().isEmpty()){
                    Toastie.topWarning(ForgotPasswordActivity.this, "Email Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                    lottieProgressDialog.dismiss();
                }else{
                    resetPassword(binding.etEmail.getText().toString());
                }
            }
        });
    }

    public void resetPassword(String email){
        apiService.forgotPassword(email).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() && response.isSuccessful()){
                    Toastie.topSuccess(ForgotPasswordActivity.this, "Reset Password Berhasil, Cek Email Anda", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toastie.topWarning(ForgotPasswordActivity.this, "Email Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toastie.topWarning(ForgotPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ForgotPasswordActivity", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}