package com.example.flowerapp.model.response;

import com.example.flowerapp.model.data.Bank;
import com.example.flowerapp.model.data.Meta;

import java.util.List;

public class GetRekening {
    private Meta meta;
    private List<Bank> data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Bank> getData() {
        return data;
    }

    public void setData(List<Bank> data) {
        this.data = data;
    }
}
