package com.example.flowerapp.util;


import java.text.NumberFormat;
import java.util.Locale;

public class ConvertCurrency {

    // Method helper statis untuk memformat angka menjadi Rupiah
    public static String formatToRupiah(int number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeID);
        String formattedCurrency = currencyFormatter.format(number);
        formattedCurrency = formattedCurrency.replace("Rp", "Rp ");
        return formattedCurrency;
    }
}
