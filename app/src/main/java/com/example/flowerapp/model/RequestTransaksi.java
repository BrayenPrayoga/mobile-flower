package com.example.flowerapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestTransaksi {
    private String total_harga_transaksi;
    private String id_kupon;

    @SerializedName("id_produk[]")
    private String id_produk;

    @SerializedName("jumlah[]")
    private String jumlah;

    @SerializedName("total_harga[]")
    private String total_harga;

    public String getTotal_harga_transaksi() {
        return total_harga_transaksi;
    }

    public void setTotal_harga_transaksi(String total_harga_transaksi) {
        this.total_harga_transaksi = total_harga_transaksi;
    }

    public String getId_kupon() {
        return id_kupon;
    }

    public void setId_kupon(String id_kupon) {
        this.id_kupon = id_kupon;
    }

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(String total_harga) {
        this.total_harga = total_harga;
    }
}
