package com.example.flowerapp.ui.cart;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flowerapp.R;
import com.example.flowerapp.databinding.ActivityCartBinding;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.response.GetCheckout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private ApiService apiService;
    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        retrofit = RClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);

        getListCart();
        binding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void getListCart(){
        apiService.listCart().enqueue(new Callback<GetCheckout>() {
            @Override
            public void onResponse(Call<GetCheckout> call, Response<GetCheckout> response) {
                if(response.isSuccessful() && response.body().getData().size() > 0){
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
                    binding.rvItemCart.setLayoutManager(linearLayoutManager);
                    CartAdapter cartAdapter = new CartAdapter(CartActivity.this, response.body().getData());
                    binding.rvItemCart.setAdapter(cartAdapter);
                }

            }

            @Override
            public void onFailure(Call<GetCheckout> call, Throwable t) {
                Log.d("CartActivity", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListCart();
    }
}