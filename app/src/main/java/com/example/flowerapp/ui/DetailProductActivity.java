package com.example.flowerapp.ui;

import android.content.Intent;
import android.graphics.Color;
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

import com.bumptech.glide.Glide;
import com.devhoony.lottieproegressdialog.LottieProgressDialog;
import com.example.flowerapp.R;
import com.example.flowerapp.databinding.ActivityDetailProductBinding;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.data.PostCheckout;
import com.example.flowerapp.util.ConvertCurrency;
import com.example.flowerapp.util.SharedPrefManager;
import com.mrntlu.toastie.Toastie;
import com.yonder.basketlayout.BasketLayoutViewListener;
import com.yonder.basketlayout.State;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity {

    private ActivityDetailProductBinding binding;
    private ApiService apiService;
    private SharedPrefManager prefManager;
    private LottieProgressDialog lottieProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        lottieProgressDialog = new LottieProgressDialog(this,
                false, null, null, null,
                null, LottieProgressDialog.SAMPLE_6, null, null);

        prefManager = new SharedPrefManager(DetailProductActivity.this);
        String token = prefManager.getToken();
        apiService = RClient.getRetrofitInstanceWithAuth(token).create(ApiService.class);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent data = getIntent();
        int idProduk = data.getIntExtra("id-produk", 0);
        String nama = data.getStringExtra("nama-produk");
        String deskripsi = data.getStringExtra("deskripsi-produk");
        String gambar = data.getStringExtra("gambar-produk");
        String harga = data.getStringExtra("harga-produk");
        int stok = data.getIntExtra("stok-produk", 0);
        String xStok = stok + "";

        String convertedPrice = ConvertCurrency.formatToRupiah(Integer.parseInt(harga));


        binding.tvNamaProduct.setText(nama);
        binding.tvDeskripsi.setText(deskripsi);
        binding.tvHarga.setText(convertedPrice);
        binding.tvStok.setText("Stok: " + xStok);

        Glide.with(DetailProductActivity.this)
                .load(RClient.getBaseUrl() + gambar)
                .placeholder(androidx.transition.R.drawable.abc_text_cursor_material)
                .error(androidx.transition.R.drawable.notification_tile_bg)
                .into(binding.ivProduct);

        binding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(idProduk);
            }
        });

        if(stok != 0 && stok > 0){
            binding.basketQty.setMaxQuantity(stok);
        }

        binding.basketQty.setBasketLayoutListener(new BasketLayoutViewListener() {
            @Override
            public void onClickDecreaseQuantity(int i) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.basketQty.setBasketQuantity(i, State.None);
                    }
                }, 500);
            }

            @Override
            public void onClickIncreaseQuantity(int i) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(stok != 0 && stok > 0){
                            binding.basketQty.setBasketQuantity(i, State.None);
                        }else{
                            binding.basketQty.setBasketQuantity(0, State.None);
                            Toastie.topWarning(DetailProductActivity.this, "Stok Kosong", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, 500);
            }

            @Override
            public void onClickTrash() {

            }

            @Override
            public void onExceedMaxQuantity(int i) {

            }
        });
    }

    public void addToCart(int idProduk){
        lottieProgressDialog.show();

        Log.d("DetailProductActivity", "Basket: " + binding.basketQty.getTvQuantity$lib_release().getText().toString());
        String quantity = binding.basketQty.getTvQuantity$lib_release().getText().toString();

        apiService.checkout(String.valueOf(idProduk), quantity).enqueue(new Callback<com.example.flowerapp.model.response.PostCheckout>() {
            @Override
            public void onResponse(Call<com.example.flowerapp.model.response.PostCheckout> call, Response<com.example.flowerapp.model.response.PostCheckout> response) {
                PostCheckout data = response.body().getData();
                Log.d("DetailProductActivity", "onResponse: " + response.isSuccessful());
                Log.d("DetailProductActivity", "onResponse: " + response.code());

                Toastie.topSuccess(DetailProductActivity.this, "Pesanan Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                lottieProgressDialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(Call<com.example.flowerapp.model.response.PostCheckout> call, Throwable t) {
                Toastie.topWarning(DetailProductActivity.this, "Pesanan Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                Log.d("DetailProductActivity", "onFailure: " + t.getMessage());
            }
        });
    }
}