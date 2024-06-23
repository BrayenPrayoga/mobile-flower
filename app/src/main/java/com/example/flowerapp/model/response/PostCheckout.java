package com.example.flowerapp.model.response;

import com.example.flowerapp.model.data.Meta;

public class PostCheckout {
    private Meta meta;
    private com.example.flowerapp.model.data.PostCheckout data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public com.example.flowerapp.model.data.PostCheckout getData() {
        return data;
    }

    public void setData(com.example.flowerapp.model.data.PostCheckout data) {
        this.data = data;
    }
}
