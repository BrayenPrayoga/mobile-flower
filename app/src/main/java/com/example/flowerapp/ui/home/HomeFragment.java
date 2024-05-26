package com.example.flowerapp.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.flowerapp.R;
import com.example.flowerapp.databinding.FragmentHomeBinding;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.BannerData;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.util.SharedPrefManager;
import com.sunzn.banner.library.Banner;

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
//        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        prefManager = new SharedPrefManager(getContext());
        String token = prefManager.getToken();
        apiService = RClient.getRetrofitInstanceWithAuth(token).create(ApiService.class);

        Log.d("HomeFragment", "token: "+ token);
        List<Bean> packs = new ArrayList<>();
        apiService.getBanner().enqueue(new Callback<com.example.flowerapp.model.response.Banner>() {
            @Override
            public void onResponse(Call<com.example.flowerapp.model.response.Banner> call, Response<com.example.flowerapp.model.response.Banner> response) {
                List<BannerData> data = response.body().getData();
                Log.d("HomeFragment", "banyakData: "+ data.size());
                for (BannerData bannerData : data)
                {
                    Log.d(TAG, "url gambar: " + bannerData.getGambar());
                    Log.d(TAG, "url penuh: " + RClient.getBaseUrl() + bannerData.getGambar());
                    packs.add(new Bean("http://192.168.1.6:8000" + bannerData.getGambar()));
                };

                binding.banner.setBannerData(packs);
            }

            @Override
            public void onFailure(Call<com.example.flowerapp.model.response.Banner> call, Throwable t) {

            }
        });

        binding.banner.setOnItemClickListener(new Banner.OnItemClickListener() {
            @Override
            public void onItemClick(int i, Object o) {
                Toast.makeText(getContext(), "position = " + i, Toast.LENGTH_SHORT).show();
            }
        });

        binding.banner.setOnItemBindListener(new Banner.OnItemBindListener<Bean>(){
            @Override
            public void onItemBind(int i, Bean bean, @NonNull ImageView imageView) {
                Glide.with(HomeFragment.this)
                        .load(bean.url)
                        .placeholder(androidx.transition.R.drawable.abc_text_cursor_material)
                        .error(androidx.transition.R.drawable.notification_tile_bg)
                        .into(imageView);
            }
        });

        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();

        getLifecycle().addObserver(binding.banner);

        binding.banner.setDefaultGainColor(Color.RED);
        binding.banner.setIndicatorGravity(GravityCompat.END);
        binding.banner.setIndicatorMargin(15);

        return root;
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
        binding.banner.setPlaying(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.banner.setPlaying(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}