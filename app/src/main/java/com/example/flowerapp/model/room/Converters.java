package com.example.flowerapp.model.room;

import androidx.room.TypeConverter;

import com.example.flowerapp.model.data.DetailProduk;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public String fromDetailProdukList(List<DetailProduk> detail) {
        if (detail == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<DetailProduk>>() {}.getType();
        return gson.toJson(detail, type);
    }

    @TypeConverter
    public List<DetailProduk> toDetailProdukList(String detailString) {
        if (detailString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<DetailProduk>>() {}.getType();
        return gson.fromJson(detailString, type);
    }
}
