package com.example.memder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyMemesFragment extends Fragment {
    private String token;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_mymemes,container,false);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        token = prefs.getString("token", "Token not found");

        Call<ApiResponse> call =
                RetrofitClient.getInstance()
                .getApi()
                .getAnswers(1,"Token "+token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                System.out.println(response.body().count);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });

        return view;
    }
}

