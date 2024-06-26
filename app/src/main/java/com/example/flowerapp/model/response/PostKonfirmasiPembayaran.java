package com.example.flowerapp.model.response;

public class PostKonfirmasiPembayaran {
    private int id_users;
    private String no_order;
    private String bank_asal;
    private String bank_tujuan;
    private String metode;
    private String nominal;
    private String tanggal;
    private String bukti;
    private String created_at;
    private String updated_at;
    private int id;

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

    public String getBank_asal() {
        return bank_asal;
    }

    public void setBank_asal(String bank_asal) {
        this.bank_asal = bank_asal;
    }

    public String getBank_tujuan() {
        return bank_tujuan;
    }

    public void setBank_tujuan(String bank_tujuan) {
        this.bank_tujuan = bank_tujuan;
    }

    public String getMetode() {
        return metode;
    }

    public void setMetode(String metode) {
        this.metode = metode;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
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
}
