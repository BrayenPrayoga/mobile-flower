package com.example.flowerapp.model.response;

import com.example.flowerapp.model.data.Checkout;
import com.example.flowerapp.model.data.Meta;

import java.util.List;

public class GetCheckout {
    private Meta meta;
    private List<Checkout> data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Checkout> getData() {
        return data;
    }

    public void setData(List<Checkout> data) {
        this.data = data;
    }
}
