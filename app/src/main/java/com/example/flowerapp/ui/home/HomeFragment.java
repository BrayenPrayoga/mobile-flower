package com.example.flowerapp.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.flowerapp.R;
import com.example.flowerapp.databinding.FragmentHomeBinding;
import com.sunzn.banner.library.Banner;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();

        getLifecycle().addObserver(binding.banner);

        binding.banner.setDefaultGainColor(Color.RED);
        binding.banner.setIndicatorGravity(GravityCompat.END);
        binding.banner.setIndicatorMargin(15);

        List<Bean> packs = new ArrayList<>();
        packs.add(new Bean("http://desk.fd.zol-img.com.cn/t_s1024x768c5/g5/M00/0D/01/ChMkJ1gq00WIXw_GAA47r_8gjqgAAXxJAH8qOMADjvH566.jpg"));
        packs.add(new Bean("http://desk.fd.zol-img.com.cn/t_s1024x768c5/g5/M00/0B/0D/ChMkJ1e9jHqIWT4CAA2dKPU9Js8AAUsZgMf8mkADZ1A116.jpg"));
        packs.add(new Bean("http://desk.fd.zol-img.com.cn/t_s1024x768c5/g5/M00/0B/0D/ChMkJle9jIGIMgtdAAYnBOEz3LAAAUsZwPgFgYABicc437.jpg"));
        packs.add(new Bean("http://desk.fd.zol-img.com.cn/t_s1024x768c5/g5/M00/0F/0A/ChMkJleZ8-iIBbFBAAVrdxItOlQAAT76QAFx7oABWuP846.jpg"));
        packs.add(new Bean("http://desk.fd.zol-img.com.cn/t_s1024x768c5/g5/M00/0B/04/ChMkJ1bG5kyIcwkXAAsM0s9DJzoAAKsAwJB9ncACwzq207.jpg"));

        binding.banner.setBannerData(packs);
        binding.banner.setOnItemClickListener(new Banner.OnItemClickListener() {
            @Override
            public void onItemClick(int i, Object o) {
                Toast.makeText(getContext(), "position = " + i, Toast.LENGTH_SHORT).show();
            }
        });

        binding.banner.setOnItemBindListener(new Banner.OnItemBindListener<Bean>() {
            @Override
            public void onItemBind(int i, Bean item, @NonNull ImageView imageView) {
                Glide.with(HomeFragment.this)
                        .load(item.url)
                        .placeholder(androidx.transition.R.drawable.abc_text_cursor_material)
                        .error(androidx.transition.R.drawable.notification_tile_bg)
                        .into(imageView);
            }
        });

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