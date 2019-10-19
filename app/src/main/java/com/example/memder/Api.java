package com.example.memder;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Api {
    @GET("/memes/api/memesbyuser/")
    Call<ApiResponse> getAnswers(@Query("page") int page, @Header("Authorization") String token);
}
