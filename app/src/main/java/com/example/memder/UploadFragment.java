package com.example.memder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class UploadFragment extends Fragment {
    View view ;
    File imageFile ;
    private static int RESULT_LOAD_IMAGE = 1;

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
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


    private void uploadImage(File imageFile){
        final TextView statusView =  (TextView) view.findViewById(R.id.status);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        SharedPreferences prefs = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        String tokenFromStorage = prefs.getString("token", "Token not found");
        client.addHeader("Authorization", "Token " + tokenFromStorage);
        try {
            params.put("img", imageFile);
            System.out.println("image in params");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("text", "Mem from android");
        client.post("http://memnderapi.pythonanywhere.com/memes/api/create/", params,new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                statusView.setText("Загружается");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                System.out.println("OK-----");
                statusView.setText("Загружено");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if (errorResponse != null) {
                    System.out.println(new String(errorResponse));
                    statusView.setText("Не удалось загрузить");
                }
            }

            @Override
            public void onFinish() {
                // called before request is finish
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            if (ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, 1);

        final Uri selectedImageURI = data.getData();
        imageFile = new File(getRealPathFromURI(selectedImageURI));

        ImageView imageView = (ImageView) view.findViewById(R.id.previewImage);
        imageView.setImageURI(selectedImageURI);
        TextView statusView = (TextView) view.findViewById(R.id.status);
        statusView.setText("Теперь нажмите кнопку загрузки");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload,container,false);
        Button mUpload = (Button) view.findViewById(R.id.UploadImage);
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(imageFile);
            }
        });

        Button mChoose = (Button) view.findViewById(R.id.chooseMem);
        mChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        return view;
    }
}
