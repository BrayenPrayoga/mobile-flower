package com.example.flowerapp.model.response;

import com.example.flowerapp.model.User;
import com.example.flowerapp.model.data.Meta;

public class RegistUser {
    private Meta meta;
    private User data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
