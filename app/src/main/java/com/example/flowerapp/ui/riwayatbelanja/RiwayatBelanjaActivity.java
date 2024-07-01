package com.example.flowerapp.ui.riwayatbelanja;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devhoony.lottieproegressdialog.LottieProgressDialog;
import com.example.flowerapp.R;
import com.example.flowerapp.databinding.ActivityRiwayatBelanjaBinding;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.data.ListTransaksi;
import com.example.flowerapp.model.response.GetTransaksi;

import com.example.flowerapp.util.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RiwayatBelanjaActivity extends AppCompatActivity {

    private ActivityRiwayatBelanjaBinding binding;
    private ApiService apiService;
    private SharedPrefManager prefManager;
    private LottieProgressDialog lottieProgressDialog;
    private List<ListTransaksi> listRiwayatBelanja;
    private RiwayatBelanjaAdapter riwayatBelanjaAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRiwayatBelanjaBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lottieProgressDialog = new LottieProgressDialog(this,
                false, null, null, null,
                null, LottieProgressDialog.SAMPLE_6, null, null);

        prefManager = new SharedPrefManager(RiwayatBelanjaActivity.this);
        String token = prefManager.getToken();
        apiService = RClient.getRetrofitInstanceWithAuth(token).create(ApiService.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RiwayatBelanjaActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvRiwayatBelanja.setLayoutManager(linearLayoutManager);
        listRiwayatBelanja = new ArrayList<>();
        riwayatBelanjaAdapter = new RiwayatBelanjaAdapter(listRiwayatBelanja, RiwayatBelanjaActivity.this);
        binding.rvRiwayatBelanja.setAdapter(riwayatBelanjaAdapter);

        fetchRiwayatBelanja();
    }

    public void fetchRiwayatBelanja(){
        lottieProgressDialog.show();
        apiService.getListTransaksi("2").enqueue(new Callback<GetTransaksi>() {
            @Override
            public void onResponse(Call<GetTransaksi> call, Response<GetTransaksi> response) {
                if(response.isSuccessful()){
                    riwayatBelanjaAdapter.fetchData(response.body().getData());

                }
                lottieProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetTransaksi> call, Throwable t) {
                lottieProgressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchRiwayatBelanja();
    }


}