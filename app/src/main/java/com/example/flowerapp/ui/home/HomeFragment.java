package com.example.flowerapp.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.flowerapp.databinding.FragmentHomeBinding;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.data.Banner;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.response.GetBanner;
import com.example.flowerapp.model.response.GetKategori;
import com.example.flowerapp.model.data.Kategori;
import com.example.flowerapp.util.SharedPrefManager;
import com.mrntlu.toastie.Toastie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ApiService apiService;
    private SharedPrefManager prefManager;
    private static final String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefManager = new SharedPrefManager(getContext());
        String token = prefManager.getToken();
        apiService = RClient.getRetrofitInstanceWithAuth(token).create(ApiService.class);

        Log.d("HomeFragment", "token: "+ token);
        getDataBanner();
        getDataProduk();

        binding.banner.setOnItemBindListener(new com.sunzn.banner.library.Banner.OnItemBindListener<Bean>(){
            @Override
            public void onItemBind(int i, Bean bean, @NonNull ImageView imageView) {
                Glide.with(HomeFragment.this)
                        .load(bean.url)
                        .placeholder(androidx.transition.R.drawable.abc_text_cursor_material)
                        .error(androidx.transition.R.drawable.notification_tile_bg)
                        .into(imageView);
            }
        });

        binding.banner.setOnItemClickListener(new com.sunzn.banner.library.Banner.OnItemClickListener() {
            @Override
            public void onItemClick(int i, Object o) {
                Toast.makeText(getContext(), "position = " + i, Toast.LENGTH_SHORT).show();
            }
        });

        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();

        getLifecycle().addObserver(binding.banner);

        binding.banner.setDefaultGainColor(Color.RED);
        binding.banner.setIndicatorGravity(GravityCompat.END);
        binding.banner.setIndicatorMargin(15);

    }

    public void getDataBanner(){
        List<Bean> packs = new ArrayList<>();
        apiService.getBanner().enqueue(new Callback<GetBanner>() {
            @Override
            public void onResponse(Call<GetBanner> call, Response<GetBanner> response) {
                List<Banner> data = response.body().getData();
                if(data.size() > 0 && response.body().getData() != null){
                    for (Banner bannerData : data)
                    {
                        packs.add(new Bean("http://192.168.1.6:8000" + bannerData.getGambar()));
                    };
                    binding.banner.setBannerData(packs);
                }else {

                }
            }

            @Override
            public void onFailure(Call<GetBanner> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });
    }

    public void getDataProduk(){
        apiService.getKategori().enqueue(new Callback<GetKategori>() {
            @Override
            public void onResponse(Call<GetKategori> call, Response<GetKategori> response) {
                Log.d(TAG, "getProduk: " + response.body());
                Log.d(TAG, "sucees: " + response.isSuccessful());

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (!response.body().getData().isEmpty()) {
                        List<Kategori> listKategori = response.body().getData();
                        if (!listKategori.isEmpty()) {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            binding.rvListKategori.setLayoutManager(linearLayoutManager);
                            KategoriAdapter kategoriAdapter = new KategoriAdapter(getContext(), listKategori);
                            binding.rvListKategori.setAdapter(kategoriAdapter);

                            binding.shimmerViewContainer.setVisibility(View.GONE);
                            binding.shimmerViewContainer.stopShimmer();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<GetKategori> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }



    private class Bean {
        String url;

        Bean(String url) {
            this.url = url;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();
        binding.banner.setPlaying(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.shimmerViewContainer.stopShimmer();
        binding.banner.setPlaying(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}