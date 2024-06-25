package com.example.flowerapp.model.response;

import com.example.flowerapp.model.data.Kupon;
import com.example.flowerapp.model.data.Meta;

public class GetKupon {
    private Meta meta;
    private Kupon data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Kupon getData() {
        return data;
    }

    public void setData(Kupon data) {
        this.data = data;
    }
}
