package com.example.memder;



import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.kevinsawicki.http.HttpRequest;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    private String adress = "https://memnderapi.pythonanywhere.com/memes/api/get/";
    private String likelink = "https://memnderapi.pythonanywhere.com/memes/api/like/";
    final OkHttpClient client = new OkHttpClient();
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay;
        String tokenFromStorage = urls[0];
        String like=urls[1];
        if (like=="1" || like=="-1"){
            RequestBody formBodylike = new FormBody.Builder()
                    .add("like",like)
                    .build();
            Request requestlike = new Request.Builder()
                    .url(likelink)
                    .addHeader("Authorization", "Token " + tokenFromStorage)
                    .post(formBodylike)
                    .build();
            try {
                Response response = client.newCall(requestlike).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bitmap mIcon11 = null;
        try {
            RequestBody formBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(adress)
                    .addHeader("Authorization", "Token " + tokenFromStorage)
                    .post(formBody)
                    .build();


            Response response = client.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());
            urldisplay = "https://memnderapi.pythonanywhere.com" + String.valueOf(json.get("img"));

            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private String adress = "https://memnderapi.pythonanywhere.com/memes/api/get/";
    private String tokenFromStorage;
    private int i;
    private String imgURL;
    private Bitmap image;
    private  Button mLike;
    private  Button mDis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLike = (Button) findViewById(R.id.Like);
        mDis = (Button) findViewById(R.id.Dis);
        final Map<String, String> data = new HashMap<String, String>();
        SharedPreferences prefs = getSharedPreferences("token", MODE_PRIVATE);
        tokenFromStorage = prefs.getString("token", "Token not found");

        new DownloadImageTask((ImageView) findViewById(R.id.image))
                .execute(tokenFromStorage,"0");
        final OkHttpClient client = new OkHttpClient();


        mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DownloadImageTask((ImageView) findViewById(R.id.image))
                        .execute(tokenFromStorage,"1");
                final OkHttpClient client = new OkHttpClient();

            }
        });

        mDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadImageTask((ImageView) findViewById(R.id.image))
                        .execute(tokenFromStorage,"-1");
                final OkHttpClient client = new OkHttpClient();
            }
        });

    }




    public void logoutUser(View view) {
        SharedPreferences prefs = getSharedPreferences("token", MODE_PRIVATE);
        prefs.edit().clear().commit();
        Intent intent = new Intent(MainActivity.this, LoginOrRegistrationActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}