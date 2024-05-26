package com.example.flowerapp.ui.home;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.BannerData;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.response.Banner;
import com.example.flowerapp.util.SharedPrefManager;
import com.mrntlu.toastie.Toastie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<BannerData>> dataBanner;
    private ApiService apiService;
    private Context mContext;

    public HomeViewModel(Context context) {
        this.mContext = context;
        SharedPrefManager prefManager = SharedPrefManager.getInstance(context);
        String token = prefManager.getToken();
        apiService = RClient.getRetrofitInstanceWithAuth(token).create(ApiService.class);
        getBannerData();
    }

    public LiveData<List<BannerData>> getBannerData(){
        dataBanner = new MutableLiveData<>();
        loadDataBanner();

        return dataBanner;

    }

    private void loadDataBanner(){
        apiService.getBanner().enqueue(new Callback<Banner>() {
            @Override
            public void onResponse(Call<Banner> call, Response<Banner> response) {
                if(response.isSuccessful()){
                    dataBanner.setValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<Banner> call, Throwable t) {
                Toastie.topWarning(mContext, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}