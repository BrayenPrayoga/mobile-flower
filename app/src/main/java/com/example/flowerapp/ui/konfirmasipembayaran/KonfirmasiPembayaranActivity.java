package com.example.flowerapp.ui.konfirmasipembayaran;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.flowerapp.R;
import com.example.flowerapp.databinding.ActivityKonfirmasiPembayaranBinding;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.response.GetTransaksi;
import com.example.flowerapp.ui.cart.CartActivity;
import com.example.flowerapp.ui.cart.CartAdapter;
import com.example.flowerapp.util.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class KonfirmasiPembayaranActivity extends AppCompatActivity {

    private ActivityKonfirmasiPembayaranBinding binding;

    private ApiService apiService;
    private SharedPrefManager prefManager;
    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKonfirmasiPembayaranBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prefManager = new SharedPrefManager(KonfirmasiPembayaranActivity.this);
        String token = prefManager.getToken();
        retrofit = RClient.getRetrofitInstanceWithAuth(token);
        apiService = retrofit.create(ApiService.class);

        apiService.getListTransaksi("0").enqueue(new Callback<GetTransaksi>() {
            @Override
            public void onResponse(Call<GetTransaksi> call, Response<GetTransaksi> response) {
                if (response.isSuccessful() && response.body().getData().size() > 0){
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(KonfirmasiPembayaranActivity.this, LinearLayoutManager.VERTICAL, false);
                    binding.rvListKonfirmasi.setLayoutManager(linearLayoutManager);
                    KonfirmasiPembayaranAdapter cartAdapter = new KonfirmasiPembayaranAdapter(response.body().getData(), KonfirmasiPembayaranActivity.this );
                    binding.rvListKonfirmasi.setAdapter(cartAdapter);
                }
            }

            @Override
            public void onFailure(Call<GetTransaksi> call, Throwable t) {
                Log.d("KonfirmasiPembayaranActivity", "onFailure: " + t.getMessage());
            }
        });

    }
}