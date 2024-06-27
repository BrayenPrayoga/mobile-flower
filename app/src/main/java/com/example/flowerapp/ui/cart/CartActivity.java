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
import com.example.flowerapp.model.data.Checkout;
import com.example.flowerapp.model.response.GetCheckout;
import com.example.flowerapp.util.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private ApiService apiService;
    private SharedPrefManager prefManager;
    private Retrofit retrofit;
    private List<Checkout> listCheckout;
    CartAdapter cartAdapter;
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

        prefManager = new SharedPrefManager(CartActivity.this);
        String token = prefManager.getToken();
        retrofit = RClient.getRetrofitInstanceWithAuth(token);
        apiService = retrofit.create(ApiService.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvItemCart.setLayoutManager(linearLayoutManager);
        listCheckout = new ArrayList<>();
        cartAdapter = new CartAdapter(CartActivity.this, listCheckout);
        binding.rvItemCart.setAdapter(cartAdapter);

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
                    listCheckout.clear();
                    listCheckout.addAll(response.body().getData());
                    cartAdapter.notifyDataSetChanged();
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

    @Override
    protected void onStart() {
        super.onStart();
    }


}