package com.example.memder;



import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.kevinsawicki.http.HttpRequest;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private String adress = "https://memnderapi.pythonanywhere.com/memes/api/get/";
    private String tokenFromStorage;
    private int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Map<String, String> data = new HashMap<String, String>();
        SharedPreferences prefs = getSharedPreferences("token", MODE_PRIVATE);
        tokenFromStorage = prefs.getString("token", "Token not found");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    data.put("Authorization", " Token " + tokenFromStorage);
                    HttpRequest response = HttpRequest.post(adress).headers(data);
                    int status = response.code();
                    System.out.println(tokenFromStorage);
                    System.out.println("@@@@@@@@@@@@@@@@@ " + status);

                    if(status == 200){
                        JSONObject json = new JSONObject(response.body());
                        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                        System.out.println(json.get("img"));
                    }else {
                        JSONObject json = new JSONObject(response.body());
                        System.out.println(json.get("detail"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        al = new ArrayList<>();
        al.add("php");
        al.add("c");
        al.add("python");
        al.add("java");
        al.add("html");
        al.add("c++");
        al.add("css");
        al.add("javascript");


        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);


        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                al.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void logoutUser(View view) {
        System.out.println("TEEEEEEEEEEEEEEEEEEEEEEESTT");
        Intent intent = new Intent(MainActivity.this, LoginOrRegistrationActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}