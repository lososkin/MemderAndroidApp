package com.example.memder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginOrRegistrationActivity extends AppCompatActivity {

    private Button mLogin, mRegister;
    private static final String addresToken = "https://memnderapi.pythonanywhere.com/signin_and_signup/api/checktoken/";
    private static String addres = "https://memnderapi.pythonanywhere.com/signin_and_signup/api/login/";
    public String tokenFromStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_registration);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("token", MODE_PRIVATE);
                tokenFromStorage = prefs.getString("token", "Error");

                //prefs.edit().clear().commit();

                if (tokenFromStorage!="Error"){
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("Authorization", " Token " + tokenFromStorage);
                    System.out.println("__________________ " + tokenFromStorage);

                    HttpRequest response = HttpRequest.post(addresToken).headers(data);
                    int status = response.code();
                    System.out.println("RESPONCE CODE: " + status);
                    String token = response.body();
                    try {
                        JSONObject json = new JSONObject(token);
                        if(status == 200){
                            System.out.println("OPEN ACTIVITY WITH TOKEN");
                            Intent intent = new Intent(LoginOrRegistrationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            System.out.println(json.get("detail"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mRegister = (Button) findViewById(R.id.Register);
        mLogin = (Button) findViewById(R.id.Login);
        final EditText mEmail = (EditText) findViewById(R.id.email);
        final EditText mPassword = (EditText) findViewById(R.id.Password);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> data = new HashMap<String, String>();
                        try {

                            data.put("username", String.valueOf(mEmail.getText()));
                            data.put("password", String.valueOf(mPassword.getText()));

                            HttpRequest response = HttpRequest.post(addres).form(data);
                            int status = response.code();
                            String token = response.body();

                            SharedPreferences prefs = getSharedPreferences("token", MODE_PRIVATE);
                            //prefs.edit().clear();
                            String tokenFromStorage = prefs.getString("token", "Token not found");

                            //Log.i("token_from_storage",tokenFromStorage);

                            JSONObject json = new JSONObject(token);
                            if(status == 200){
                                SharedPreferences.Editor editor = getSharedPreferences("token", MODE_PRIVATE).edit();
                                editor.putString("token", String.valueOf(json.get("token")));
                                editor.apply();
                                System.out.println("ACCEPT AND SAVE TOKEN");
                                System.out.println(json.get("token"));
                                Intent intent = new Intent(LoginOrRegistrationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                System.out.println("KEK");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });



        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginOrRegistrationActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}
