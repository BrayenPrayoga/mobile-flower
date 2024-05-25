package com.example.flowerapp.model.response;

import com.example.flowerapp.model.User;

/**
 * Created by Ardian Iqbal Yusmartito on 25/05/24
 * Github : https://github.com/ALU-syntax
 * Twitter : https://twitter.com/mengkerebe
 * Instagram : https://www.instagram.com/ardian_iqbal_
 * LinkedIn : https://www.linkedin.com/in/ardianiqbal
 */
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
