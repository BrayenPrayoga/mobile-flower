package com.example.flowerapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.flowerapp.databinding.FragmentProfilesBinding;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.User;
import com.example.flowerapp.model.data.Logout;
import com.example.flowerapp.ui.LoginActivity;
import com.example.flowerapp.util.SharedPrefManager;
import com.mrntlu.toastie.Toastie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileFragment extends Fragment {

    private FragmentProfilesBinding binding;

    private SharedPrefManager sharedPrefManager;
    private User user;
    private UpdateProfileDialog updateProfileDialog;
    private ApiService apiService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfilesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPrefManager = new SharedPrefManager(getContext());
        String token = sharedPrefManager.getToken();
        user = sharedPrefManager.getUser();

        apiService = RClient.getRetrofitInstanceWithAuth(token).create(ApiService.class);

        binding.tvNama.setText(user.getName());
        updateProfileDialog = new UpdateProfileDialog(getContext());
        binding.btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                updateProfileDialog.show(fragmentManager, "UpdateProfile");
            }
        });
        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.logout().enqueue(new Callback<Logout>() {
                    @Override
                    public void onResponse(Call<Logout> call, Response<Logout> response) {
                        sharedPrefManager.clearToken();
                        Toastie.topSuccess(getContext(), "Berhasil Logout", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(Call<Logout> call, Throwable t) {
                        Log.d("ProfileFragment", "onFailure: " + t.getMessage());
                        Toastie.topWarning(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}