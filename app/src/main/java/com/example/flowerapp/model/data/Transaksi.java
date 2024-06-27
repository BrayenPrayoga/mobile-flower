package com.example.flowerapp.model.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

public class Transaksi {

    private int id_users;
    private String no_order;

    private int status_transaksi;
    private String tanggal_transaksi;
    private String total_harga_transaksi;
    private String id_kupon;
    private String alamat;
    private String created_at;
    private String updated_at;
    private int id;
//    private List<DetailProduk> detail;

    public int getId_users() {
        return id_users;
    }

    public void setId_users(int id_users) {
        this.id_users = id_users;
    }

    public String getNo_order() {
        return no_order;
    }

    public void setNo_order(String no_order) {
        this.no_order = no_order;
    }

    public int getStatus_transaksi() {
        return status_transaksi;
    }

    public void setStatus_transaksi(int status_transaksi) {
        this.status_transaksi = status_transaksi;
    }

    public String getTanggal_transaksi() {
        return tanggal_transaksi;
    }

    public void setTanggal_transaksi(String tanggal_transaksi) {
        this.tanggal_transaksi = tanggal_transaksi;
    }

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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public List<DetailProduk> getDetail() {
//        return detail;
//    }
//
//    public void setDetail(List<DetailProduk> detail) {
//        this.detail = detail;
//    }
}
