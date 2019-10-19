package com.example.memder;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDataSource extends PageKeyedDataSource<Integer, Item> {

    //the size of a page that we want
    public static final int PAGE_SIZE = 5;

    //we will start from the first page which is 1
    private static final int FIRST_PAGE = 1;

    //we need to fetch from stackoverflow


    //this will be called once to load the initial data
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Item> callback) {
        RetrofitClient.getInstance()
                .getApi().getAnswers(1,"Token 45caec1f88281b0531f0026ddf5f110fb5fc3cc7") //TODO token must be for user
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.body() != null) {
                            callback.onResult(response.body().results, null, FIRST_PAGE + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                    }
                });
    }

    //this will load the previous page
    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Item> callback) {
        RetrofitClient.getInstance()
                .getApi().getAnswers(params.key,"Token 45caec1f88281b0531f0026ddf5f110fb5fc3cc7") //TODO token must be for user
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        //if the current page is greater than one
                        //we are decrementing the page number
                        //else there is no previous page
                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (response.body() != null) {

                            //passing the loaded data
                            //and the previous page key
                            callback.onResult(response.body().results, adjacentKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                    }
                });
    }

    //this will load the next page
    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Item> callback) {
        RetrofitClient.getInstance()
                .getApi()
                .getAnswers(params.key, "Token 45caec1f88281b0531f0026ddf5f110fb5fc3cc7") //TODO token must be for user
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        if (response.body() != null) {
                            //if the response has next page
                            //incrementing the next page number
                            Integer key = response.body().next!=null ? params.key + 1 : null;

                            //passing the loaded data and next page value
                            callback.onResult(response.body().results, key);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                    }
                });
    }
}
