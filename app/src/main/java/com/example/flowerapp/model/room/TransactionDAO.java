package com.example.flowerapp.model.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.flowerapp.model.data.Transaksi;

import java.util.List;

@Dao
public interface TransactionDAO {
    @Insert
    void insert(Transaksi transaksi);

    @Query("SELECT * FROM 'transaction'")
    List<Transaksi> getAllTransaction();
}
