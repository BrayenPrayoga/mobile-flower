package com.example.flowerapp.ui.cart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flowerapp.R;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.data.Checkout;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    private Context context;
    public List<Checkout> listCheckout;

    public CartAdapter(Context context){
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Checkout checkout = listCheckout.get(position);

        holder.tvProduct.setText(checkout.getRelasi_produk().getProduk());
        holder.tvQty.setText(checkout.getJumlah());
        holder.tvPrice.setText(checkout.getRelasi_produk().getHarga());

        Glide.with(context)
                .load(RClient.getBaseUrl() + checkout.getRelasi_produk().getGambar())
                .placeholder(androidx.transition.R.drawable.abc_text_cursor_material)
                .error(androidx.transition.R.drawable.notification_tile_bg)
                .into(holder.ivProduct);

        holder.btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return listCheckout.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvProduct, tvPrice, tvQty;
        AppCompatButton btnBayar;
        ImageView ivProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProduct = itemView.findViewById(R.id.tv_nama_product);
            tvPrice = itemView.findViewById(R.id.tv_harga);
            tvQty = itemView.findViewById(R.id.tv_qty);
            btnBayar = itemView.findViewById(R.id.btn_bayar);
            ivProduct = itemView.findViewById(R.id.iv_product);

        }
    }

}
