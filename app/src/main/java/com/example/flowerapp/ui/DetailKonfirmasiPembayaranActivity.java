package com.example.flowerapp.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.devhoony.lottieproegressdialog.LottieProgressDialog;
import com.example.flowerapp.R;
import com.example.flowerapp.databinding.ActivityDetailKonfirmasiPembayaranBinding;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.data.Bank;
import com.example.flowerapp.model.response.GetRekening;
import com.example.flowerapp.model.response.PostKonfirmasiPembayaran;
import com.example.flowerapp.ui.cart.CartActivity;
import com.example.flowerapp.util.ConvertCurrency;
import com.example.flowerapp.util.SharedPrefManager;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mrntlu.toastie.Toastie;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailKonfirmasiPembayaranActivity extends AppCompatActivity {

    private ActivityDetailKonfirmasiPembayaranBinding binding;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;

    private ApiService apiService;
    private Retrofit retrofit;
    private SharedPrefManager prefManager;
    private List<String> listRekening;
    private List<Bank> listBank;
    private LottieProgressDialog lottieProgressDialog;
    private String bankTujuanTerpilih;
    private static final String TAG = "DetailKonfirmasiPembayaran";
    private int idProduk;
    private String namaProduk;
    private String hargaProduk;
    private String gambarProduk;
    private int jumlahProduk;
    private String kupon;
    private int subTotal;
    private float diskon;
    private int total;
    private String noOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailKonfirmasiPembayaranBinding.inflate(getLayoutInflater());
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
        EdgeToEdge.enable(this);

        Intent dataIntent = getIntent();
        idProduk = dataIntent.getIntExtra("id-produk", 0);
        namaProduk = dataIntent.getStringExtra("nama-produk");
        hargaProduk = dataIntent.getStringExtra("harga-produk");
        gambarProduk = dataIntent.getStringExtra("gambar-produk");
        jumlahProduk = dataIntent.getIntExtra("jumlah-produk", 0);
        kupon = dataIntent.getStringExtra("kupon");
        noOrder = dataIntent.getStringExtra("no-order");

        prefManager = new SharedPrefManager(DetailKonfirmasiPembayaranActivity.this);
        String token = prefManager.getToken();
        retrofit = RClient.getRetrofitInstanceWithAuth(token);
        apiService = retrofit.create(ApiService.class);

        binding.tvNamaProduct.setText(namaProduk);
        binding.tvHarga.setText("Harga: " + ConvertCurrency.formatToRupiah((int) Float.parseFloat(hargaProduk)));
        binding.tvJumlah.setText("x"+ String.valueOf(jumlahProduk));
        Glide.with(DetailKonfirmasiPembayaranActivity.this)
                .load(RClient.getBaseUrl() + gambarProduk)
                .placeholder(androidx.transition.R.drawable.abc_text_cursor_material)
                .error(androidx.transition.R.drawable.notification_tile_bg)
                .into(binding.ivProduct);


        float convertHargaProduk = Float.parseFloat(hargaProduk);
        subTotal = jumlahProduk * (int) convertHargaProduk;
        diskon = kupon.toString().isEmpty() ? 0 : Float.parseFloat(kupon);
        total = subTotal - (int) diskon;

        binding.tvNominalSubutotal.setText(ConvertCurrency.formatToRupiah(subTotal));
        binding.tvNominalDiskon.setText(ConvertCurrency.formatToRupiah((int) diskon));
        binding.tvNominalTotal.setText(ConvertCurrency.formatToRupiah(total));
        binding.spinnerRekening.setHint("Pilih Nomor Rekening Tujuan");

        binding.etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        binding.btnGaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        apiService.getNorek().enqueue(new Callback<GetRekening>() {
            @Override
            public void onResponse(Call<GetRekening> call, Response<GetRekening> response) {
                if (response.isSuccessful() && response.body().getData().size() > 0){
                    listBank = new ArrayList<>();
                    listRekening = new ArrayList<>();
                    for(Bank data : response.body().getData()){
                        listRekening.add(data.getJenis() + " - " + data.getNomer_rekening());
                        listBank.add(data);

                    }

                    binding.spinnerRekening.setItems(listRekening);
                }

            }

            @Override
            public void onFailure(Call<GetRekening> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        binding.spinnerRekening.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Bank bankTerpilih = listBank.get(position);
                bankTujuanTerpilih = bankTerpilih.getJenis();
            }
        });

        binding.btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.etBankAsal.getText().toString().equals("")){
                    binding.etBankAsal.setError("Bank Asal Tidak Boleh Kosong");
                    YoYo.with(Techniques.Tada)
                            .duration(1000)
                            .repeat(1)
                            .playOn(binding.etBankAsal);
                }else if(binding.etTanggal.getText().toString().isEmpty()){
                    Toastie.topError(DetailKonfirmasiPembayaranActivity.this, "Masukan Tanggal Transfer", Toast.LENGTH_SHORT).show();
                    binding.etTanggal.setError("Masukan Tanggal Transfer");
                    YoYo.with(Techniques.Tada)
                            .duration(1000)
                            .repeat(1)
                            .playOn(binding.etTanggal);
                }else if(bankTujuanTerpilih == null){
                    YoYo.with(Techniques.Tada)
                            .duration(1000)
                            .repeat(1)
                            .playOn(binding.spinnerRekening);
                }else if(imageUri == null){
                    Toastie.topWarning(DetailKonfirmasiPembayaranActivity.this, "Masukan Bukti Transfer Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }else{
                    lottieProgressDialog.show();
                    konfirmasiPembayaran(
                            binding.etBankAsal.getText().toString(),
                            bankTujuanTerpilih,
                            "Transfer",
                            String.valueOf(total),
                            binding.etTanggal.getText().toString(),
                            noOrder
                    );
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            binding.ivPreview.setImageURI(imageUri);
            binding.ivPreview.setVisibility(View.VISIBLE);
        }
    }

    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        } else {
            return null;
        }
    }

    public void konfirmasiPembayaran(
            String bankAsal,
            String bankTUjuan,
            String metode,
            String nominal,
            String tanggal,
            String noOrder
    ){
        File file = new File(getPath(imageUri));
        RequestBody requestFoto = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("bukti", file.getName(), requestFoto);

        RequestBody requestBankAsal = RequestBody.create(MediaType.parse("text/plain"), bankAsal);
        RequestBody requestBankTujuan = RequestBody.create(MediaType.parse("text/plain"), bankTUjuan);
        RequestBody requestMetode = RequestBody.create(MediaType.parse("text/plain"), metode);
        RequestBody requestNominal = RequestBody.create(MediaType.parse("text/plain"), nominal);
        RequestBody requestTanggal = RequestBody.create(MediaType.parse("text/plain"), tanggal);
        RequestBody requestNoOrder = RequestBody.create(MediaType.parse("text/plain"), noOrder);


        apiService.postKonfirmasiPembayaran(requestBankAsal, requestBankTujuan, requestMetode, requestNominal, requestTanggal, body, requestNoOrder)
                .enqueue(new Callback<PostKonfirmasiPembayaran>() {
            @Override
            public void onResponse(Call<PostKonfirmasiPembayaran> call, Response<PostKonfirmasiPembayaran> response) {
                lottieProgressDialog.dismiss();

                Toastie.topSuccess(DetailKonfirmasiPembayaranActivity.this, "Konfirmasi Pembayaran Berhasil", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<PostKonfirmasiPembayaran> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                lottieProgressDialog.dismiss();
            }
        });

    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(DetailKonfirmasiPembayaranActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // month is 0-based, so add 1
                        String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        binding.etTanggal.setText(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
}