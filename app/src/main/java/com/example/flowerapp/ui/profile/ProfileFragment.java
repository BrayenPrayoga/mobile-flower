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
import com.example.flowerapp.model.data.Checkout;
import com.example.flowerapp.model.data.ListTransaksi;
import com.example.flowerapp.model.data.Logout;
import com.example.flowerapp.model.response.GetCheckout;
import com.example.flowerapp.model.response.GetTransaksi;
import com.example.flowerapp.ui.LoginActivity;
import com.example.flowerapp.ui.cart.CartActivity;
import com.example.flowerapp.ui.konfirmasipembayaran.KonfirmasiPembayaranActivity;
import com.example.flowerapp.ui.riwayatbelanja.RiwayatBelanjaActivity;
import com.example.flowerapp.util.SharedPrefManager;
import com.mrntlu.toastie.Toastie;

import java.util.List;

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

        getNotifKonfirmasiPembayaran();
        getNotifCart();

        binding.tvNama.setText(user.getName());
        updateProfileDialog = new UpdateProfileDialog(getContext());

        binding.btnRiwayatBelanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RiwayatBelanjaActivity.class);
                startActivity(intent);
            }
        });
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
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);

                hideBadgeCart();
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

        binding.btnKonfirmasiPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), KonfirmasiPembayaranActivity.class);
                startActivity(intent);

                hideBadgeKonfirmasiPembayaran();
            }
        });
    }

    private void showBadgeCart(int count){
        if (count > 0){
            binding.badgeCart.setText(String.valueOf(count));
            binding.badgeCart.setVisibility(View.VISIBLE);
            binding.btnCart.setZ(1);
            binding.badgeCart.setZ(2);
        }else{
            binding.badgeCart.setVisibility(View.INVISIBLE);
        }
    }

    private void hideBadgeCart(){
        binding.badgeCart.setVisibility(View.INVISIBLE);
    }
    private void showBadgeKonfirmasiPembayaran(int count){
        if(count > 0){
            binding.badgeKonfirmasiPembayaran.setText(String.valueOf(count));
            binding.badgeKonfirmasiPembayaran.setVisibility(View.VISIBLE);
            binding.btnKonfirmasiPembayaran.setZ(1);
            binding.badgeKonfirmasiPembayaran.setZ(2);
        }else{
            binding.badgeKonfirmasiPembayaran.setVisibility(View.INVISIBLE);
        }
    }

    private void hideBadgeKonfirmasiPembayaran(){
        binding.badgeKonfirmasiPembayaran.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getNotifCart();
        getNotifKonfirmasiPembayaran();
    }

    public void getNotifCart(){
        apiService.listCart().enqueue(new Callback<GetCheckout>() {
            @Override
            public void onResponse(Call<GetCheckout> call, Response<GetCheckout> response) {
                if(response.isSuccessful() && response.body().getData().size() > 0){
                    List<Checkout> data = response.body().getData();

                    showBadgeCart(data.size());
                }
            }

            @Override
            public void onFailure(Call<GetCheckout> call, Throwable t) {
                Log.d("ProfileFragment", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getNotifKonfirmasiPembayaran(){
        apiService.getListTransaksi("0").enqueue(new Callback<GetTransaksi>() {
            @Override
            public void onResponse(Call<GetTransaksi> call, Response<GetTransaksi> response) {
                List<ListTransaksi> listTransaksi = response.body().getData();
                Log.d("ProfileFragment", "listKonfirmasiPembayaran: " + listTransaksi.size());

                showBadgeKonfirmasiPembayaran(listTransaksi.size());
            }

            @Override
            public void onFailure(Call<GetTransaksi> call, Throwable t) {
                Log.d("ProfileFragment", "onFailure: " + t.getMessage());
            }
        });
    }
}