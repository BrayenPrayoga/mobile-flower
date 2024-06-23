package com.example.flowerapp.model.response;

import com.example.flowerapp.model.data.Checkout;
import com.example.flowerapp.model.data.Meta;

public class PostCheckout {
    private Meta meta;
    private Checkout data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Checkout getData() {
        return data;
    }

    public void setData(Checkout data) {
        this.data = data;
    }
}
