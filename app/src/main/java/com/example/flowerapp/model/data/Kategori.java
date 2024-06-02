package com.example.flowerapp.model.data;

import java.util.List;

public class Kategori {
    private int id;
    private String kategori;
    private List<Produk> produk;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public List<Produk> getProduk() {
        return produk;
    }

    public void setProduk(List<Produk> produk) {
        this.produk = produk;
    }
}
