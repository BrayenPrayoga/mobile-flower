package com.example.flowerapp.model.response;

import com.example.flowerapp.model.data.Meta;
import com.example.flowerapp.model.data.Transaksi;

public class PostTransaksi {
    private String meta;
    private Transaksi data;

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public Transaksi getData() {
        return data;
    }

    public void setData(Transaksi data) {
        this.data = data;
    }
}
