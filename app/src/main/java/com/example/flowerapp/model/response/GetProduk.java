package com.example.flowerapp.model.response;

import com.example.flowerapp.model.data.Meta;
import com.example.flowerapp.model.data.Produk;

import java.util.List;

public class GetProduk {
    private Meta meta;
    private List<Produk> data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Produk> getData() {
        return data;
    }

    public void setData(List<Produk> data) {
        this.data = data;
    }
}
