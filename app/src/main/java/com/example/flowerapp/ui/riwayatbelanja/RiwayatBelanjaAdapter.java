package com.example.flowerapp.ui.riwayatbelanja;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flowerapp.R;
import com.example.flowerapp.model.data.DetailTransaksi;
import com.example.flowerapp.model.data.ListTransaksi;
import com.example.flowerapp.ui.DetailKonfirmasiPembayaranActivity;
import com.example.flowerapp.ui.konfirmasipembayaran.KonfirmasiPembayaranAdapter;

import java.util.List;

public class RiwayatBelanjaAdapter extends RecyclerView.Adapter<RiwayatBelanjaAdapter.ViewHolder>{

    private List<ListTransaksi> listTransaksi;
    private Context context;
    int idProduk = 0;
    String namaProduk = "";
    String hargaProduk = "";
    String gambarProduk = "";
    int jumlahProduk = 0;
    String kupon = "";
    private static final String TAG = "KonfirmasiPembayaranAdapter";

    public RiwayatBelanjaAdapter(List<ListTransaksi> listTransaksi, Context context) {
        this.listTransaksi = listTransaksi;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat_belanja, parent, false);
        return new RiwayatBelanjaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListTransaksi transaksi = listTransaksi.get(position);

        if (listTransaksi.size() > 0 && transaksi.getDetail_transaksi() != null){

            if(transaksi.getKupon() != null){
                kupon = transaksi.getKupon().getKredit();
            }

            holder.tvKodeProduct.setText("Kode Produk: " + transaksi.getNo_order());
            for(DetailTransaksi data: transaksi.getDetail_transaksi()){
                holder.tvProduct.setText("Nama Product: " + data.getProduk());
                holder.tvQty.setText("Qty: " + data.getJumlah());
                holder.tvHarga.setText("Harga: " + data.getTotal_harga());

                idProduk = data.getId_produk();
                namaProduk = data.getProduk();
                hargaProduk = data.getTotal_harga();
                gambarProduk = data.getGambar();
                jumlahProduk = data.getJumlah();

            }

            holder.btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailKonfirmasiPembayaranActivity.class);
                    intent.putExtra("id-produk", idProduk);
                    intent.putExtra("nama-produk", namaProduk);
                    intent.putExtra("harga-produk", hargaProduk);
                    intent.putExtra("gambar-produk", gambarProduk);
                    intent.putExtra("jumlah-produk", jumlahProduk);
                    intent.putExtra("kupon", kupon);
                    intent.putExtra("no-order", transaksi.getNo_order());

                    context.startActivity(intent);
                }
            });
        }

    }

    public void fetchData(List<ListTransaksi> dataBaru){
        listTransaksi.clear();
        listTransaksi.addAll(dataBaru);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listTransaksi.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvProduct, tvQty, tvHarga, tvKodeProduct, tvDiskonTerpakai;

        AppCompatButton btnKonfirmasi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProduct = itemView.findViewById(R.id.tv_nama_product);
            tvQty = itemView.findViewById(R.id.tv_qty);
            tvHarga = itemView.findViewById(R.id.tv_harga);
            tvKodeProduct = itemView.findViewById(R.id.tv_kode_product);
            tvDiskonTerpakai = itemView.findViewById(R.id.tv_diskon_terpakai);
            btnKonfirmasi = itemView.findViewById(R.id.btn_konfirmasi);
        }
    }
}
