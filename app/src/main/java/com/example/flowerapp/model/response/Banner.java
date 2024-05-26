package com.example.flowerapp.model.response;


import com.example.flowerapp.model.BannerData;

import java.util.List;

public class Banner {
    private Meta meta;
    private List<BannerData> data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<BannerData> getData() {
        return data;
    }

    public void setData(List<BannerData> data) {
        this.data = data;
    }
}
