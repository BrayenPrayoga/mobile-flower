package com.example.flowerapp.ui.explore;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flowerapp.R;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.data.Produk;
import com.example.flowerapp.util.ConvertCurrency;

import java.util.ArrayList;
import java.util.List;

public class ExploreProdukAdapter extends RecyclerView.Adapter<ExploreProdukAdapter.ViewHolder> implements Filterable {

    private List<Produk> listProduk;
    private List<Produk> listProdukFull;
    private Context context;

    public ExploreProdukAdapter(List<Produk> listProduk, Context context) {
        this.listProduk = listProduk;
        this.context = context;
        listProdukFull = new ArrayList<>(listProduk);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produk data = listProduk.get(position);
        String convertedPrice = ConvertCurrency.formatToRupiah(Integer.parseInt(data.getHarga()));

        holder.tvTitle.setText(data.getProduk());
        holder.tvHarga.setText("Harga: " + convertedPrice);

        Glide.with(context)
                .load(RClient.getBaseUrl() + data.getGambar())
                .placeholder(androidx.transition.R.drawable.abc_text_cursor_material)
                .error(androidx.transition.R.drawable.notification_tile_bg)
                .into(holder.ivProduk);
    }

    @Override
    public int getItemCount() {
        return listProduk.size();
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Produk> filteredProduk = new ArrayList<>();

            if(constraint.length() == 0){
                filteredProduk.addAll(listProdukFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Produk item : listProdukFull) {
                    if (item.getProduk().toString().toLowerCase().contains(filterPattern)) {
                        filteredProduk.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredProduk;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listProduk.clear();
            listProduk.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvHarga;
        ImageView ivProduk;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_nama_product);
            tvHarga = itemView.findViewById(R.id.tv_price);
            ivProduk = itemView.findViewById(R.id.iv_product);
        }
    }
}
