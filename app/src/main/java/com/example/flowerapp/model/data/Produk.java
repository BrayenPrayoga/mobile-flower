package com.example.flowerapp.model.data;
public class Produk {
    private String produk;
    private String gambar;
    private String deskripsi;
    private String harga;
    private int stok;

    public String getProduk() {
        return produk;
    }

    public void setProduk(String produk) {
        this.produk = produk;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
}
