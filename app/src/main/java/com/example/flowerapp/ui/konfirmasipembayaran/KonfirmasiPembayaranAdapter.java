package com.example.flowerapp.ui.konfirmasipembayaran;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flowerapp.R;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.data.DetailTransaksi;
import com.example.flowerapp.model.data.ListTransaksi;
import com.example.flowerapp.model.data.Produk;
import com.example.flowerapp.ui.DetailTransactionActivity;
import com.example.flowerapp.ui.home.ProdukAdapter;

import java.util.List;

public class KonfirmasiPembayaranAdapter extends RecyclerView.Adapter<KonfirmasiPembayaranAdapter.ViewHolder> {

    private List<ListTransaksi> listTransaksi;
    private Context context;
    private static final String TAG = "KonfirmasiPembayaranAdapter";


    public KonfirmasiPembayaranAdapter(List<ListTransaksi> listTransaksi, Context context) {
        this.listTransaksi = listTransaksi;
        this.context = context;
    }

    @NonNull
    @Override
    public KonfirmasiPembayaranAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_konfirmasi_pembayaran, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KonfirmasiPembayaranAdapter.ViewHolder holder, int position) {
        ListTransaksi transaksi = listTransaksi.get(position);

        if (listTransaksi.size() > 0 && transaksi.getDetail_transaksi() != null){
            holder.tvKodeProduct.setText("Kode Produk: " + transaksi.getNo_order());
            for(DetailTransaksi data: transaksi.getDetail_transaksi()){
                holder.tvProduct.setText("Nama Product: " + data.getProduk());
                holder.tvQty.setText("Qty: " + data.getJumlah());
                holder.tvHarga.setText("Harga: " + data.getTotal_harga());

            }
        }

    }

    @Override
    public int getItemCount() {
        return listTransaksi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvProduct, tvQty, tvHarga, tvKodeProduct;

        AppCompatButton btnKonfirmasi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProduct = itemView.findViewById(R.id.tv_nama_product);
            tvQty = itemView.findViewById(R.id.tv_qty);
            tvHarga = itemView.findViewById(R.id.tv_harga);
            tvKodeProduct = itemView.findViewById(R.id.tv_kode_product);
            btnKonfirmasi = itemView.findViewById(R.id.btn_konfirmasi);
        }
    }
}
