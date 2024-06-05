package com.example.flowerapp.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.flowerapp.databinding.FragmentProfilesBinding;
import com.example.flowerapp.model.User;
import com.example.flowerapp.util.SharedPrefManager;

public class ProfileFragment extends Fragment {

    private FragmentProfilesBinding binding;

    private SharedPrefManager sharedPrefManager;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfilesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPrefManager = new SharedPrefManager(getContext());
        user = sharedPrefManager.getUser();

        binding.tvNama.setText(user.getName());

        binding.btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}