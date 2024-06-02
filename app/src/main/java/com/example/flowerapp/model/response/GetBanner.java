package com.example.flowerapp.model.response;


import com.example.flowerapp.model.data.Banner;
import com.example.flowerapp.model.data.Meta;

import java.util.List;

public class GetBanner {
    private Meta meta;
    private List<Banner> data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Banner> getData() {
        return data;
    }

    public void setData(List<Banner> data) {
        this.data = data;
    }
}
