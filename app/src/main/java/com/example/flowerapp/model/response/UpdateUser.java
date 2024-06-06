package com.example.flowerapp.model.response;

import com.example.flowerapp.model.User;
import com.example.flowerapp.model.data.Meta;

public class UpdateUser {
    private Meta meta;
    private User user;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
