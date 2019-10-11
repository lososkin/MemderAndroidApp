package com.example.memder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private Button mLogin;
    private EditText mEmail, mPassword;
    private static String addres = "https://memnderapi.pythonanywhere.com/signin_and_signup/api/login/";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        mLogin = (Button) findViewById(R.id.Login);
//        mEmail = (EditText) findViewById(R.id.email);
//        mPassword = (EditText) findViewById(R.id.Password);
//        Button mBack = (Button) findViewById(R.id.BackL);
//
//
//        mLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AsyncTask.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        Map<String, String> data = new HashMap<String, String>();
//                        try {
//
//                            data.put("username", String.valueOf(mEmail.getText()));
//                            data.put("password", String.valueOf(mPassword.getText()));
//
//                            HttpRequest response = HttpRequest.post(addres).form(data);
//                            int status = response.code();
//                            String token = response.body();
//
//                            SharedPreferences prefs = getSharedPreferences("token", MODE_PRIVATE);
//                            //prefs.edit().clear();
//                            String tokenFromStorage = prefs.getString("token", "Token not found");
//
//                            //Log.i("token_from_storage",tokenFromStorage);
//
//                            JSONObject json = new JSONObject(token);
//                            if(status == 200){
//                                SharedPreferences.Editor editor = getSharedPreferences("token", MODE_PRIVATE).edit();
//                                editor.putString("token", String.valueOf(json.get("token")));
//                                editor.apply();
//                                System.out.println("ACCEPT AND SAVE TOKEN");
//                                System.out.println(json.get("token"));
//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                            else{
//                                System.out.println("KEK");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
//
//        mBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, LoginOrRegistrationActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

    }
}
