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
    private int totalHarga;
    private int idBankSelected;

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

        retrofit = RClient.getRetrofitInstance();
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

        binding.tvNamaProduct.setText(namaProduk);
        binding.tvDesc.setText(descProduk);
        binding.tvHarga.setText("Harga: " + ConvertCurrency.formatToRupiah(Integer.parseInt(hargaProduk)));
        binding.tvJumlah.setText("Jumlah: "+ String.valueOf(jumlahPembelian));

        Glide.with(DetailTransactionActivity.this)
                .load(RClient.getBaseUrl() + "img_produk/" + gambarProduk)
                .placeholder(androidx.transition.R.drawable.abc_text_cursor_material)
                .error(androidx.transition.R.drawable.notification_tile_bg)
                .into(binding.ivProduct);


        setTextWithDots(binding.tvSubtotalProduk, "Subtotal", ConvertCurrency.formatToRupiah(totalHarga));
        setTextWithDots(binding.tvDiskon, "Diskon", ConvertCurrency.formatToRupiah(0));
        setTextWithDots(binding.tvTotal, "Total", ConvertCurrency.formatToRupiah(totalHarga));
        binding.btnConfirmPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kode = binding.etPromo.getText().toString();
                cariPromo(kode);
            }
        });

        idBankSelected = 0;

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

                binding.spinnerRekening.setItems(data);
                binding.spinnerRekening.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        idBankSelected = idBank.get(position);
                    }
                });
            }

            @Override
            public void onFailure(Call<GetRekening> call, Throwable t) {
                Log.d("DetailTransactionActivity", "onFailure: " + t.getMessage());
            }
        });

        binding.btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idKupon = idBankSelected > 0 ? idBankSelected : 0;
                List<String> paramIdProduk = new ArrayList<>();
                paramIdProduk.add(String.valueOf(idProduk));

                List<String> paramJumlah = new ArrayList<>();
                paramJumlah.add(String.valueOf(jumlahPembelian));

                List<String> paramTotalHarga = new ArrayList<>();
                paramTotalHarga.add(hargaProduk);

                submitTransaksi(
                        binding.tvTotal.getText().toString(),
                        String.valueOf(idKupon),
                        String.valueOf(idProduk),
                        String.valueOf(jumlahPembelian),
                        hargaProduk
                );

            }
        });
    }

    public void cariPromo(String kode){
        apiService.getKupon(kode).enqueue(new Callback<GetKupon>() {
            @Override
            public void onResponse(Call<GetKupon> call, Response<GetKupon> response) {
                Kupon kupon = response.body().getData();
                if (response.isSuccessful() && response.body().getData() != null){
                    setTextWithDots(binding.tvDiskon, "Diskon", "Rp." + kupon.getKredit());
                    float total = totalHarga - Float.parseFloat(kupon.getKredit());
                    setTextWithDots(binding.tvTotal, "Total", ConvertCurrency.formatToRupiah((int) total));
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

    private void setTextWithDots(TextView textView, String label, String value) {
        int totalLength = 100; // Total length including dots

        String text = label + " " + value;
        int numDots = totalLength - text.length();

        StringBuilder dotBuilder = new StringBuilder();
        for (int i = 0; i < numDots; i++) {
            dotBuilder.append(".");
        }

        String finalText = label + " " + dotBuilder.toString() + " " + value;
        textView.setText(finalText);
    }

    private void submitTransaksi(
            String totalHargaTransaksi,
            String idKupon,
            String idProduk,
            String jumlah,
            String totalHarga
    ){
        apiService.buatTransaksi(totalHargaTransaksi, idKupon, idProduk, jumlah, totalHarga).enqueue(new Callback<PostTransaksi>() {
            @Override
            public void onResponse(Call<PostTransaksi> call, Response<PostTransaksi> response) {
                if (response.isSuccessful() && response.body().getData() != null){
                    Transaksi data = response.body().getData();

                    Transaksi transaksi = new Transaksi(
                            data.getNo_order(),
                            data.getId_users(),
                            data.getStatus_transaksi(),
                            data.getTanggal_transaksi(),
                            data.getTotal_harga_transaksi(),
                            data.getId_kupon(),
                            data.getCreated_at(),
                            data.getUpdated_at(),
                            data.getId(),
                            data.getDetail()
                    );
//                    transaksi.setId_users(data.getId_users());
//                    transaksi.setNo_order(data.getNo_order());
//                    transaksi.setStatus_transaksi(data.getStatus_transaksi());
//                    transaksi.setTanggal_transaksi(data.getTanggal_transaksi());
//                    transaksi.setTotal_harga_transaksi(data.getTotal_harga_transaksi());
//                    transaksi.setId_kupon(data.getId_kupon());
//                    transaksi.setCreated_at(data.getCreated_at());
//                    transaksi.setUpdated_at(data.getUpdated_at());
//                    transaksi.setId(data.getId());
//                    transaksi.setDetail(data.getDetail());

                    Toastie.topSuccess(DetailTransactionActivity.this,"Pesanan Berhasil dikonfirmasi silahkan lakukan pembayaram.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PostTransaksi> call, Throwable t) {
                Log.d("DetailTransactionActivity", "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });

//        RequestTransaksi request = new RequestTransaksi();
//
//        request.setTotal_harga_transaksi("100000");
//        request.setId_kupon("KU12345");
//        request.setId_produk("1");
//        request.setJumlah("1");
//        request.setTotal_harga("5000");

//        apiService.testTransaksi(request).enqueue(new Callback<PostTransaksi>() {
//            @Override
//            public void onResponse(Call<PostTransaksi> call, Response<PostTransaksi> response) {
//
//                Log.d("DetailTransactionActivity", "onResponse: " + response.body());
//                Log.d("DetailTransactionActivity", "onResponse: " + response.errorBody().toString());
//                Log.d("DetailTransactionActivity", "onResponse: " + response.raw());
//                Log.d("DetailTransactionActivity", "onResponse: " + response.isSuccessful());
//            }
//
//            @Override
//            public void onFailure(Call<PostTransaksi> call, Throwable t) {
//                Log.d("DetailTransactionActivity", "onFailure: "+ t.getMessage());
//
//            }
//        });

    }
}