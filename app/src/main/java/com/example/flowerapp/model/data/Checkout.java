package com.example.flowerapp.model.data;


public class Checkout {
    private int id;
    private int id_users;
    private int id_produk;
    private int jumlah;
    private String created_at;
    private String updated_at;
    private RelasiProduk relasi_produk;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_users() {
        return id_users;
    }

    public void setId_users(int id_users) {
        this.id_users = id_users;
    }

    public int getId_produk() {
        return id_produk;
    }

    public void setId_produk(int id_produk) {
        this.id_produk = id_produk;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public RelasiProduk getRelasi_produk() {
        return relasi_produk;
    }

    public void setRelasi_produk(RelasiProduk relasi_produk) {
        this.relasi_produk = relasi_produk;
    }
}
