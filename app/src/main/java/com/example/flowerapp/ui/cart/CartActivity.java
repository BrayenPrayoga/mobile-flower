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

import com.devhoony.lottieproegressdialog.LottieProgressDialog;
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
    private LottieProgressDialog lottieProgressDialog;
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
        lottieProgressDialog = new LottieProgressDialog(this,
                false, null, null, null,
                null, LottieProgressDialog.SAMPLE_6, null, null);

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
        lottieProgressDialog.show();
        apiService.listCart().enqueue(new Callback<GetCheckout>() {
            @Override
            public void onResponse(Call<GetCheckout> call, Response<GetCheckout> response) {
                if(response.isSuccessful()){
                    cartAdapter.fetchData(response.body().getData());
                }
                lottieProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetCheckout> call, Throwable t) {
                Log.d("CartActivity", "onFailure: " + t.getMessage());
                lottieProgressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("CartActivity", "onResume: OnResume Calld" );
        getListCart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}