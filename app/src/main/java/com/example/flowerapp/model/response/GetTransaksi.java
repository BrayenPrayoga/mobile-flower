package com.example.flowerapp.model.response;

import com.example.flowerapp.model.data.ListTransaksi;
import com.example.flowerapp.model.data.Meta;

import java.util.List;

public class GetTransaksi {
    private Meta meta;
    private List<ListTransaksi> data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<ListTransaksi> getData() {
        return data;
    }

    public void setData(List<ListTransaksi> data) {
        this.data = data;
    }
}
