package com.example.memder;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
    Button mLike;
    Button mDis;
    private String adress = Settings.host+"/memes/api/get/";
    private String likelink = Settings.host+"/memes/api/like/";
    final OkHttpClient client = new OkHttpClient();
    public DownloadImageTask(ImageView bmImage, Button mLike, Button mDis) {
        this.bmImage = bmImage;
        this.mDis=mDis;
        this.mLike=mLike;

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
        Matrix matrix = new Matrix();
        //set image rotation value to 90 degrees in matrix.

        try {
            RequestBody formBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(adress)
                    .addHeader("Authorization", "Token " + tokenFromStorage)
                    .post(formBody)
                    .build();


            Response response = client.newCall(request).execute();
            if (response.code()==204)return null;
            JSONObject json = new JSONObject(response.body().string());
            urldisplay = Settings.host + String.valueOf(json.get("img"));
            System.out.println(Settings.host + String.valueOf(json.get("img")));
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            matrix.postRotate(0);
            //supply the original width and height, if you don't want to change the height and width of bitmap.
            mIcon11 = Bitmap.createBitmap(mIcon11, 0, 0, mIcon11.getWidth(),mIcon11.getHeight(), matrix, true);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (result==null){
            bmImage.setImageResource(R.drawable.nomemes);

        }
        else {
            bmImage.setImageBitmap(result);
        }
        mLike.setEnabled(true);
        mDis.setEnabled(true);
    }
}

public class MainActivity extends AppCompatActivity {
    private Fragment mainFragment;
    private Fragment uploadFragment;
    private Fragment mymemesFagment;
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private String adress = Settings.host+"/memes/api/get/";
    public static String tokenFromStorage;
    private int i;
    private String imgURL;
    private Bitmap image;
    private  Button mLike;
    private  Button mDis;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.navigation_bottom);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_memes);

        mLike = (Button) findViewById(R.id.Like);
        mDis = (Button) findViewById(R.id.Dis);
        final Map<String, String> data = new HashMap<String, String>();
        final SharedPreferences prefs = getSharedPreferences("token", MODE_PRIVATE);
        tokenFromStorage = prefs.getString("token", "Token not found");

//        imageView = findViewById(R.id.image);
//        System.out.println(imageView);
//        imageView.setImageResource(R.drawable.downloadmem);
//        mLike.setEnabled(false);
//        mDis.setEnabled(false);
//        new DownloadImageTask((ImageView) findViewById(R.id.image),mLike,mDis)
//                .execute(tokenFromStorage,"0");


//        mLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mLike.setEnabled(false);
//                mDis.setEnabled(false);
//                imageView.setImageResource(R.drawable.downloadmem);
//                new DownloadImageTask((ImageView) findViewById(R.id.image),mLike,mDis)
//                        .execute(tokenFromStorage,"1");
//
//            }
//        });
//
//        mDis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mLike.setEnabled(false);
//                mDis.setEnabled(false);
//                imageView.setImageResource(R.drawable.downloadmem);
//                new DownloadImageTask((ImageView) findViewById(R.id.image),mLike,mDis)
//                        .execute(tokenFromStorage,"-1");
//            }
//        });
//        Button mMenu = (Button) findViewById(R.id.Menu);
//
//        mMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
//                startActivity(intent);
//                finish();
//                return;
//            }
//        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
                    if(mainFragment!=null) {
                        fragmentTransaction.hide(mainFragment);
                    }
                    if(uploadFragment!=null) {
                        fragmentTransaction.hide(uploadFragment);
                    }
                    if(mymemesFagment!=null) {
                        fragmentTransaction.hide(mymemesFagment);
                    }

                    Fragment selectedFragment=null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_upload:
                            if(uploadFragment==null) {
                                uploadFragment = new UploadFragment();
                                fragmentTransaction.add(R.id.fragment_container,uploadFragment);
                            }
                            selectedFragment = uploadFragment;
                            break;
                        case R.id.nav_memes:
                            if(mainFragment==null) {
                                mainFragment = new MainFragment();
                                fragmentTransaction.add(R.id.fragment_container,mainFragment);
                            }
                            selectedFragment = mainFragment;
                            break;

                        case R.id.nav_mymemes:
                            if(mymemesFagment==null) {
                                mymemesFagment = new MyMemesFragment();
                                fragmentTransaction.add(R.id.fragment_container,mymemesFagment);
                            }
                            selectedFragment = mymemesFagment;
                            break;

                    }
                    fragmentTransaction.show(selectedFragment);
                    fragmentTransaction.commit();
//                    Fragment selectedFragment = null;
//
//                    FragmentManager mFragmentManager = getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//
//                    Fragment curFrag = mFragmentManager.getPrimaryNavigationFragment();
//                    if (curFrag != null) {
//                        fragmentTransaction.hide(curFrag);
//                    }
//
//                    Fragment fragment = mFragmentManager.findFragmentById(menuItem.getItemId());
//                    if (fragment == null) {
//                        switch (menuItem.getItemId()){
//                            case R.id.nav_upload:
//                                fragment = new UploadFragment();
//
//                                break;
//                            case R.id.nav_memes:
//                                fragment = new MainFragment();
//                                break;
//
//                            case R.id.nav_mymemes:
//                                fragment = new MyMemesFragment();
//                                break;
//                        }
//                        fragmentTransaction.add(R.id.fragment_container,fragment);
//
//                    } else {
//                        fragmentTransaction.show();
//                    }
//
//                    fragmentTransaction.setPrimaryNavigationFragment(fragment);
//                    fragmentTransaction.setReorderingAllowed(true);
//                    fragmentTransaction.commitNowAllowingStateLoss();

//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };

}