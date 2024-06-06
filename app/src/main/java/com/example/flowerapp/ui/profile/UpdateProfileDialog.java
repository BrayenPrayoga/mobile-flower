package com.example.flowerapp.ui.profile;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.devhoony.lottieproegressdialog.LottieProgressDialog;
import com.example.flowerapp.R;
import com.example.flowerapp.model.ApiService;
import com.example.flowerapp.model.RClient;
import com.example.flowerapp.model.response.UpdateUser;
import com.example.flowerapp.util.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;
import com.mrntlu.toastie.Toastie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateProfileDialog extends DialogFragment {
    TextInputEditText etName, etEmail, etPassword;
    AppCompatButton btnCancel, btnConfirm;
    ApiService apiService;
    Retrofit retrofit;
    private LottieProgressDialog lottieProgressDialog;
    private SharedPrefManager prefManager;
    Context context;

    private static final String TAG = "UpdateProfileDialog";

    public UpdateProfileDialog(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_update_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName = view.findViewById(R.id.et_name);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnConfirm = view.findViewById(R.id.btn_confirm);

        prefManager = new SharedPrefManager(getContext());
        String token = prefManager.getToken();

        Log.d(TAG, "token: " + token);
        retrofit = RClient.getRetrofitInstanceWithAuth(token);
        apiService = retrofit.create(ApiService.class);


        lottieProgressDialog = new LottieProgressDialog(getContext(),
                false, null, null, null,
                null, LottieProgressDialog.SAMPLE_6, null, null);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valid = validate();
                Log.d("UpdateProfileDialog", "onClick: " + valid);
                Log.d(TAG, "onClick: " + etName.getText());
                Log.d(TAG, "onClick: " + etEmail.getText());
                Log.d(TAG, "onClick: " + etPassword.getText());
                if(valid == 0){
                    lottieProgressDialog.show();
                    if(etPassword.getText().toString().isEmpty()){
                        Log.d(TAG, "onClick: " + "MASOK");
                        apiService.updateUser(etName.getText().toString(), etEmail.getText().toString(), "").enqueue(new Callback<UpdateUser>() {
                            @Override
                            public void onResponse(Call<UpdateUser> call, Response<UpdateUser> response) {
                                Log.d(TAG, "onResponse: " + response.body());
                                lottieProgressDialog.dismiss();
                                Toastie.topSuccess(getContext(), "Data Berhasil diupdate", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }

                            @Override
                            public void onFailure(Call<UpdateUser> call, Throwable t) {
                                Log.d("UpdateProfileDialog", "onFailure: " + t.getMessage());
                                lottieProgressDialog.dismiss();
                                Toastie.topWarning(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Log.d(TAG, "onClick: " + "MASOK PAKE PASSWORD");
                        apiService.updateUser(etName.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString()).enqueue(new Callback<UpdateUser>() {
                            @Override
                            public void onResponse(Call<UpdateUser> call, Response<UpdateUser> response) {
                                lottieProgressDialog.dismiss();
                                Toastie.topSuccess(getContext(), "Data Berhasil diupdate", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }

                            @Override
                            public void onFailure(Call<UpdateUser> call, Throwable t) {
                                Log.d("UpdateProfileDialog", "onFailure: " + t.getMessage());
                                lottieProgressDialog.dismiss();
                                Toastie.topWarning(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }
        });
        
    }

    private int validate(){
        int error = 0;
        if(etName.getText().toString().isEmpty()){
            etName.setError("Nama Tidak Boleh Kosong");
            YoYo.with(Techniques.Tada)
                    .duration(1000)
                    .repeat(1)
                    .playOn(etName);
            error++;
        }
        if(etEmail.getText().toString().isEmpty()){
            etEmail.setError("email tidak boleh kosong");
            YoYo.with(Techniques.Tada)
                    .duration(1000)
                    .repeat(1)
                    .playOn(etEmail);
            error++;
        }

        return error;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}
