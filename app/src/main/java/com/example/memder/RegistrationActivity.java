package com.example.memder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private Button mRegister;
    private EditText mEmail, mPassword,mPassword1;
    private String addressForRegistration = "https://memnderapi.pythonanywhere.com/signin_and_signup/api/signup/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mRegister = (Button) findViewById(R.id.Register);
        Button mBack = (Button) findViewById(R.id.Back);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mPassword1 = (EditText) findViewById(R.id.password1);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    Map<String, String> data = new HashMap<String, String>();
                    @Override
                    public void run() {
                        try {

                                data.put("username", String.valueOf(mEmail.getText()));
                                data.put("password", String.valueOf(mPassword.getText()));
                                data.put("password2", String.valueOf(mPassword1.getText()));


                            System.out.println("zdes"+String.valueOf(mPassword.getText())+"zdes");
                            HttpRequest response = HttpRequest.post(addressForRegistration).form(data);
                            int status = response.code();
                            String token = response.body();

                            JSONObject json = new JSONObject(token);

                            if(status == 200){
                                SharedPreferences.Editor editor = getSharedPreferences("token", MODE_PRIVATE).edit();
                                editor.putString("token", String.valueOf(json.get("token")));
                                editor.apply();
                                System.out.println("ACCEPT AND SAVE TOKEN");
                                System.out.println(json.get("token"));
                                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Iterator<?> keys = json.keys();
                                TextView mError = (TextView) findViewById(R.id.ErrorsTextReg);
                                while( keys.hasNext() ) {
                                    String key = (String) keys.next();
                                    if (key.equals("error")){
                                        mError.setText(json.get("error").toString());
                                        break;
                                    }
                                    System.out.println(key);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginOrRegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
