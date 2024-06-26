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
import com.example.flowerapp.R;
import com.example.flowerapp.databinding.ActivityRegisterBinding;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.User;
import com.example.flowerapp.model.response.RegistUser;
import com.mrntlu.toastie.Toastie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private Retrofit retrofit;
    private ApiService apiService;
    private LottieProgressDialog lottieProgressDialog;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
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

        binding.btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.etNama.getText().toString().isEmpty()){
                    Toastie.topWarning(RegisterActivity.this, "Nama Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                } else if (binding.etEmail.getText().toString().isEmpty()) {
                    Toastie.topWarning(RegisterActivity.this, "Email Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                } else if(binding.etKataSandi.getText().toString().isEmpty()){
                    Toastie.topWarning(RegisterActivity.this,"Password Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                } else if(!binding.etKataSandi.getText().toString().equals(binding.etKonfirmasiKataSandi.getText().toString())){
                    Toastie.topWarning(RegisterActivity.this, "Konfirmasi Password Salah", Toast.LENGTH_LONG).show();
                }else{
                    register(binding.etNama.getText().toString(), binding.etEmail.getText().toString(), binding.etKataSandi.getText().toString());

                }
            }
        });

        binding.txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void register(String nama, String email, String password){
        FragmentManager fragmentManager = getSupportFragmentManager();
//        loadingDialog.show(fragmentManager, "loading");
        lottieProgressDialog.show();
        //role harus selalu 1, jika role = 2 adalah admin
        String role = "1";
        Call<RegistUser> call = apiService.registUser(nama, email, password, role);
        call.enqueue(new Callback<RegistUser>() {
            @Override
            public void onResponse(Call<RegistUser> call, Response<RegistUser> response) {
                RegistUser responseBody = response.body();
                Log.d(TAG, "onResponse: " + response.message());
                Log.d(TAG, "onResponse: " + response.code());
                Log.d(TAG, "onResponse: " + response.isSuccessful());
                Log.d("RegisterActivity", "onResponse: " + responseBody);
                User dataUser = responseBody.getData();
                Toastie.topSuccess(RegisterActivity.this, "User " + dataUser.getName()
                        + " berhasil didaftarkan!", Toast.LENGTH_LONG).show();
//                loadingDialog.dismiss();
                lottieProgressDialog.dismiss();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<RegistUser> call, Throwable t) {
//                loadingDialog.dismiss();
                lottieProgressDialog.dismiss();
                Toastie.topWarning(RegisterActivity.this, "Error," + t.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }
}