package com.example.flowerapp.ui.explore;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.widget.Toast;

import com.example.flowerapp.databinding.FragmentExploreBinding;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.data.Produk;
import com.example.flowerapp.model.response.GetProduk;
import com.example.flowerapp.util.SharedPrefManager;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mrntlu.toastie.Toastie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private FragmentExploreBinding binding;
    private MaterialSearchBar searchBar;
    private SharedPrefManager prefManager;
    private ApiService apiService;
    private ExploreProdukAdapter exploreProdukAdapter;

    private static final String TAG = "ExploreFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        prefManager = new SharedPrefManager(getContext());
        String token = prefManager.getToken();
        apiService = RClient.getRetrofitInstanceWithAuth(token).create(ApiService.class);

        binding.searchBar.setOnSearchActionListener(this);
        binding.searchBar.setSpeechMode(true);

        loadProduk();

        // Periksa izin mikrofon
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
        }
        return root;
    }

    public void loadProduk(){
        apiService.getProduk().enqueue(new Callback<GetProduk>() {
            @Override
            public void onResponse(Call<GetProduk> call, Response<GetProduk> response) {
                List<Produk> produk = response.body().getData();
                if(response.isSuccessful() && response.body() != null && produk.size() > 0){
//                    binding.tvProdukKosong.setVisibility(View.GONE);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                    binding.rvDataExplore.setLayoutManager(gridLayoutManager);
                    exploreProdukAdapter = new ExploreProdukAdapter(produk, getContext());
                    binding.rvDataExplore.setAdapter(exploreProdukAdapter);
                }else{
//                    binding.tvProdukKosong.setVisibility(View.VISIBLE);
//                    binding.tvProdukKosong.setText("Produk Kosong");
                }

            }

            @Override
            public void onFailure(Call<GetProduk> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toastie.topError(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startVoiceInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Katakan Sesuatu");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception e){
            Toastie.topWarning(getContext(), "Voice input not supported", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == getActivity().RESULT_OK && data != null){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String voiceInput = result.get(0);
                searchBar.setText(voiceInput);
                if (exploreProdukAdapter != null) {
                    exploreProdukAdapter.getFilter().filter(voiceInput);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        Log.d(TAG, "onSearchStateChanged: " + enabled);

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Log.d(TAG, "onSearchConfirmed: " + text);
        if (exploreProdukAdapter != null) {
            exploreProdukAdapter.getFilter().filter(text.toString());
        }

    }

    @Override
    public void onButtonClicked(int buttonCode) {
        Log.d(TAG, "onButtonClicked: " + buttonCode);
        if (buttonCode == MaterialSearchBar.BUTTON_SPEECH) {
            startVoiceInput();
        }
    }
}