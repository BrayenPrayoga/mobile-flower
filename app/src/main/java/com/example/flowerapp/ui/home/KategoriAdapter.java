package com.example.flowerapp.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flowerapp.R;
import com.example.flowerapp.model.data.Kategori;

import java.util.List;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.ViewHolder>{

    private List<Kategori> listKategori;
    private Context context;

    public KategoriAdapter(Context context, List<Kategori> listKategori ){
        this.context = context;
        this.listKategori = listKategori;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Kategori data = listKategori.get(position);
        holder.tvTitle.setText(data.getKategori());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.rvProduk.setLayoutManager(linearLayoutManager);
        ProdukAdapter produkAdapter = new ProdukAdapter(data.getProduk(), context);
        holder.rvProduk.setAdapter(produkAdapter);

    }

    @Override
    public int getItemCount() {
        return listKategori.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        RecyclerView rvProduk;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_category);
            rvProduk = itemView.findViewById(R.id.rv_product);
        }
    }
}
