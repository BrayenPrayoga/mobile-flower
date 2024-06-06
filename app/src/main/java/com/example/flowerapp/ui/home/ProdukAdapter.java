package com.example.flowerapp.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flowerapp.R;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.data.Produk;
import com.example.flowerapp.util.ConvertCurrency;

import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ViewHolder> {

    private List<Produk> listProduk;
    private Context context;

    public ProdukAdapter(List<Produk> listProduk, Context context) {
        this.listProduk = listProduk;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produk produk = listProduk.get(position);
        String stok = String.valueOf(produk.getStok());
        String convertedPrice = ConvertCurrency.formatToRupiah(Integer.parseInt(produk.getHarga()));

        holder.tvTitle.setText(produk.getProduk());
        holder.tvPrice.setText("Harga: " + convertedPrice);

        Glide.with(context)
                .load(RClient.getBaseUrl() + produk.getGambar())
                .placeholder(androidx.transition.R.drawable.abc_text_cursor_material)
                .error(androidx.transition.R.drawable.notification_tile_bg)
                .into(holder.ivProduk);
    }

    @Override
    public int getItemCount() {
        return listProduk.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvPrice;
        ImageView ivProduk;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_nama_product);
            tvPrice = itemView.findViewById(R.id.tv_price);
            ivProduk = itemView.findViewById(R.id.iv_product);
        }
    }
}
