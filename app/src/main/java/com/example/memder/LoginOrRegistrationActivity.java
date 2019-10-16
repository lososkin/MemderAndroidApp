package com.example.memder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginOrRegistrationActivity extends AppCompatActivity {

    private Button mLogin, mRegister;
    private static final String addresToken = Settings.host+"/signin_and_signup/api/checktoken/";
    private static String addres = Settings.host+"/signin_and_signup/api/login/";
    public String tokenFromStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_registration);


        SharedPreferences prefs = getSharedPreferences("token", MODE_PRIVATE);
        tokenFromStorage = prefs.getString("token", "Error");

        //prefs.edit().clear().commit();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (tokenFromStorage != "Error") {
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("Authorization", " Token " + tokenFromStorage);
                    System.out.println("__________________ " + tokenFromStorage);

                    HttpRequest response = HttpRequest.post(addresToken).headers(data);
                    int status = response.code();
                    System.out.println("RESPONCE CODE: " + status);
                    String token = response.body();
                    try {
                        JSONObject json = new JSONObject(token);
                        if (status == 200) {
                            System.out.println("OPEN ACTIVITY WITH TOKEN");
                            Intent intent = new Intent(LoginOrRegistrationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            System.out.println(json.get("detail"));
                            Intent intent = new Intent(LoginOrRegistrationActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent(LoginOrRegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
