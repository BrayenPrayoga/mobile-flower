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
import com.example.flowerapp.ui.home.ProdukAdapter;

import java.util.List;

public class KonfirmasiPembayaranAdapter extends RecyclerView.Adapter<KonfirmasiPembayaranAdapter.ViewHolder> {

    private List<ListTransaksi> listTransaksi;
    private Context context;



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
        Log.d("KonfirmasiPembayaranAdapter", "onBindViewHolder: " + transaksi.getNominal());
        if (listTransaksi.size() > 0 && transaksi.getDetail_transaksi() != null){
//            DetailTransaksi detailTransaksi = transaksi.getDetail_transaksi().get(0);


        }

        holder.tvHarga.setText(transaksi.getNominal());
//            holder.tvProduct.setText(detailTransaksi.getProduk());
        holder.tvTanggal.setText(transaksi.getTanggal());

        Glide.with(context)
                .load(RClient.getBaseUrl() + transaksi.getBukti())
                .placeholder(androidx.transition.R.drawable.abc_text_cursor_material)
                .error(androidx.transition.R.drawable.notification_tile_bg)
                .into(holder.ivProduct);
    }

    @Override
    public int getItemCount() {
        return listTransaksi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProduct;
        TextView tvProduct, tvTanggal, tvHarga;

        AppCompatButton btnKonfirmasi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            tvProduct = itemView.findViewById(R.id.tv_nama_product);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvHarga = itemView.findViewById(R.id.tv_harga);
            btnKonfirmasi = itemView.findViewById(R.id.btn_konfirmasi);
        }
    }
}
