package com.example.flowerapp.model.data;

import java.util.List;

public class ListTransaksi {
    private int id;
    private String no_order;
    private String bank_asal;
    private String bank_tujuan;
    private String metode;
    private String nominal;
    private String tanggal;
    private String bukti;
    private int id_kupon;
    private Kupon kupon;
    private List<DetailTransaksi> detail_transaksi;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getId_kupon() {
        return id_kupon;
    }

    public void setId_kupon(int id_kupon) {
        this.id_kupon = id_kupon;
    }

    public Kupon getKupon() {
        return kupon;
    }

    public void setKupon(Kupon kupon) {
        this.kupon = kupon;
    }

    public List<DetailTransaksi> getDetail_transaksi() {
        return detail_transaksi;
    }

    public void setDetail_transaksi(List<DetailTransaksi> detail_transaksi) {
        this.detail_transaksi = detail_transaksi;
    }
}
