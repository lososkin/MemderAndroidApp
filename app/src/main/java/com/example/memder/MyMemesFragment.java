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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class  MyMemes{
    public String img;
    public int likes;
    public int dis;

    public MyMemes(String img,int likes,int dis){
        this.img = img;
        this.likes=likes;
        this.dis=dis;
    }

    public MyMemes(){
    }

    public List<MyMemes> getData(int pageNumber,String token) {
        final List<MyMemes> MyMemesList = new ArrayList<MyMemes>();

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Token " + token);

        client.get(Settings.host+"/memes/api/memesbyuser/?page="+pageNumber,new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(new String(response));
                    JSONArray memesArr = jsonResponse.getJSONArray("results");
                    for (int i = 0; i < memesArr.length(); i++)
                    {
                        String img = memesArr.getJSONObject(i).getString("img");
                        int dis = memesArr.getJSONObject(i).getInt("dislikes");
                        int likes = memesArr.getJSONObject(i).getInt("likes");
                        MyMemesList.add(i,new MyMemes(img,likes,dis));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if (errorResponse != null) {
                    System.out.println(new String(errorResponse));
                }
            }

            @Override
            public void onFinish() {
                // called before request is finish
                System.out.println(MyMemesList.size());
            }

        });
        return MyMemesList;
    }
}

public class MyMemesFragment extends Fragment {
    private String token;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_mymemes,container,false);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        token = prefs.getString("token", "Token not found");

        MyMemes a = new MyMemes();
        List<MyMemes> myMemesList = a.getData(1,token);

        return view;
    }
}

