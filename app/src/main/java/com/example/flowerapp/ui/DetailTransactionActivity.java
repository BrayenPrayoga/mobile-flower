package com.example.flowerapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.devhoony.lottieproegressdialog.LottieProgressDialog;
import com.example.flowerapp.R;
import com.example.flowerapp.databinding.ActivityDetailTransactionBinding;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.RequestTransaksi;
import com.example.flowerapp.model.data.Bank;
import com.example.flowerapp.model.data.Kupon;
import com.example.flowerapp.model.data.Transaksi;
import com.example.flowerapp.model.response.GetKupon;
import com.example.flowerapp.model.response.GetRekening;
import com.example.flowerapp.model.response.PostTransaksi;
import com.example.flowerapp.util.ConvertCurrency;
import com.example.flowerapp.util.SharedPrefManager;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mrntlu.toastie.Toastie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailTransactionActivity extends AppCompatActivity {

    private ActivityDetailTransactionBinding binding;
    private ApiService apiService;
    private Retrofit retrofit;
    private SharedPrefManager sharedPrefManager;
    private int totalHarga;
    private String idKupon;
    private float totalTransaksi;

    private LottieProgressDialog lottieProgressDialog;
    private static final String TAG = "DetailTransaksiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTransactionBinding.inflate(getLayoutInflater());
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

        sharedPrefManager = new SharedPrefManager(DetailTransactionActivity.this);
        String token = sharedPrefManager.getToken();
        retrofit = RClient.getRetrofitInstanceWithAuth(token);
        apiService = retrofit.create(ApiService.class);

        Intent dataIntent = getIntent();
        Intent data = getIntent();
        int idProduk = data.getIntExtra("id-produk", 0);
        String namaProduk = dataIntent.getStringExtra("nama-produk");
        String descProduk = dataIntent.getStringExtra("desc-produk");
        String gambarProduk = dataIntent.getStringExtra("gambar-produk");
        String hargaProduk = data.getStringExtra("harga-produk");
        int jumlahPembelian = data.getIntExtra("jumlah-pembelian", 0);

        totalHarga = Integer.parseInt(hargaProduk) * jumlahPembelian;
        totalTransaksi = totalHarga;

        binding.tvNamaProduct.setText(namaProduk);
        binding.tvDesc.setText(descProduk);
        binding.tvHarga.setText("Harga: " + ConvertCurrency.formatToRupiah(Integer.parseInt(hargaProduk)));
        binding.tvJumlah.setText("Jumlah: "+ String.valueOf(jumlahPembelian));

        Glide.with(DetailTransactionActivity.this)
                .load(RClient.getBaseUrl() + "img_produk/" + gambarProduk)
                .placeholder(androidx.transition.R.drawable.abc_text_cursor_material)
                .error(androidx.transition.R.drawable.notification_tile_bg)
                .into(binding.ivProduct);


        binding.tvNominalSubutotal.setText(ConvertCurrency.formatToRupiah(totalHarga));
        binding.tvNominalDiskon.setText(ConvertCurrency.formatToRupiah(0));
        binding.tvNominalTotal.setText(ConvertCurrency.formatToRupiah(totalHarga));

        binding.btnConfirmPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kode = binding.etPromo.getText().toString();
                cariPromo(kode);
            }
        });

        idKupon = "";

        apiService.getNorek().enqueue(new Callback<GetRekening>() {
            @Override
            public void onResponse(Call<GetRekening> call, Response<GetRekening> response) {
                ArrayList<Integer> idBank = new ArrayList<>();
                ArrayList<String> data = new ArrayList<>();
                if(response.isSuccessful() && response.body().getData().size() > 0){
                    List<Bank> bankList = response.body().getData();
                    for (Bank dataBank : bankList){
                        data.add(dataBank.getJenis() + " - " + dataBank.getNomer_rekening());
                        idBank.add(dataBank.getId());
                    }
                }

//                binding.spinnerRekening.setItems(data);
//                binding.spinnerRekening.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                        idBankSelected = idBank.get(position);
//                    }
//                });
            }

            @Override
            public void onFailure(Call<GetRekening> call, Throwable t) {
                Log.d("DetailTransactionActivity", "onFailure: " + t.getMessage());
            }
        });

        binding.btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lottieProgressDialog.show();
                if(binding.etAlamat.getText().toString().equals("")){
                    binding.etAlamat.setError("Alamat Tidak Boleh Kosong");
                    YoYo.with(Techniques.Tada)
                            .duration(1000)
                            .repeat(1)
                            .playOn(binding.etAlamat);
                    lottieProgressDialog.dismiss();
                }else{
                    Log.d(TAG, "onClick: " + binding.tvNominalTotal.getText().toString());
                    Log.d(TAG, "onClick: " + binding.etAlamat.getText().toString());
                    Log.d(TAG, "onClick: " + idKupon);
                    Log.d(TAG, "onClick: " + String.valueOf(idProduk));
                    Log.d(TAG, "onClick: " + String.valueOf(jumlahPembelian));
                    Log.d(TAG, "onClick: " + binding.tvNominalTotal.getText().toString());
                    submitTransaksi(
                            String.valueOf(totalTransaksi),
                            binding.etAlamat.getText().toString(),
                            idKupon,
                            String.valueOf(idProduk),
                            String.valueOf(jumlahPembelian),
                            String.valueOf(totalHarga)
                    );

                }



            }
        });
    }

    public void cariPromo(String kode){
        apiService.getKupon(kode).enqueue(new Callback<GetKupon>() {
            @Override
            public void onResponse(Call<GetKupon> call, Response<GetKupon> response) {
                Kupon kupon = response.body().getData();
                if (response.isSuccessful() && response.body().getData() != null){
                    idKupon = String.valueOf(kupon.getId());
                    binding.tvNominalDiskon.setText("Rp. " + kupon.getKredit());
                    float total = totalHarga - Float.parseFloat(kupon.getKredit());
                    totalTransaksi = total;
                    binding.tvNominalTotal.setText(ConvertCurrency.formatToRupiah((int) total));
                }else{
                    binding.etPromo.setError("Kode Tidak Ditemukan");
                    YoYo.with(Techniques.Tada)
                            .duration(1000)
                            .repeat(1)
                            .playOn(binding.etPromo);
                }
            }

            @Override
            public void onFailure(Call<GetKupon> call, Throwable t) {
                Log.d("DetailTransactionActivity", "onFailure: " + t.getMessage());
            }
        });
    }

    private void submitTransaksi(
            String totalHargaTransaksi,
            String alamat,
            String idKupon,
            String idProduk,
            String jumlah,
            String totalHarga
    ){
        apiService.buatTransaksi(totalHargaTransaksi, alamat, idKupon, idProduk, jumlah, totalHarga).enqueue(new Callback<PostTransaksi>() {
            @Override
            public void onResponse(Call<PostTransaksi> call, Response<PostTransaksi> response) {
                Log.d(TAG, "onResponse: " + response.isSuccessful());
                Log.d(TAG, "onResponse: " + response.body().getData().getNo_order());
                Toastie.topSuccess(DetailTransactionActivity.this,"Pesanan Berhasil dikonfirmasi silahkan lakukan pembayaram.", Toast.LENGTH_SHORT).show();
                finish();

                lottieProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PostTransaksi> call, Throwable t) {
                Toastie.topError(DetailTransactionActivity.this, "Cek Koneksi Kembali", Toast.LENGTH_SHORT).show();
                Log.d("DetailTransactionActivity", "onFailure: " + t.getMessage());
                lottieProgressDialog.dismiss();
            }
        });
    }
}