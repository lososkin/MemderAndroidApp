package com.example.memder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class MenuActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            if (ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
                String[] PERMISSIONS_STORAGE = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                ActivityCompat.requestPermissions(MenuActivity.this, PERMISSIONS_STORAGE, 1);

                final Uri selectedImageURI = data.getData();
                final File imageFile = new File(getRealPathFromURI(selectedImageURI));

        ImageView imageView = (ImageView) findViewById(R.id.UploadedImage);
        imageView.setImageURI(selectedImageURI);

                final int size = (int) imageFile.length();
                byte[] bytes = new byte[size];
                try {
                    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(imageFile));
                    buf.read(bytes, 0, bytes.length);
                    buf.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                final String Str_image = Base64.encodeToString(bytes, Base64.DEFAULT);

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();SharedPreferences prefs = getSharedPreferences("token", MODE_PRIVATE);
                String tokenFromStorage = prefs.getString("token", "Token not found");
                client.addHeader("Authorization", "Token " + tokenFromStorage);
                try {
                    params.put("img", imageFile);
                    System.out.println("image in params");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                params.put("text", "Mem from android");
                client.post(Settings.host+"/memes/api/create/", params,new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started
                    }


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"
                        System.out.println("OK-----");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        if (errorResponse != null) {
                            System.out.println(new String(errorResponse));
                        }
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });

            }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        Button mUpload = (Button) findViewById(R.id.UploadImage);
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


        Button mLogout = (Button) findViewById(R.id.Logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("token", MODE_PRIVATE);
                prefs.edit().clear().commit();
                Intent intent = new Intent(MenuActivity.this, LoginOrRegistrationActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        Button mMain = (Button) findViewById(R.id.Main);
        mMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }


}
