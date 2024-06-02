package com.example.flowerapp.model.response;

import com.example.flowerapp.model.data.Kategori;
import com.example.flowerapp.model.data.Meta;

import java.util.List;


public class GetKategori {
    private Meta meta;
    private List<Kategori> data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Kategori> getData() {
        return data;
    }

    public void setData(List<Kategori> data) {
        this.data = data;
    }
}
