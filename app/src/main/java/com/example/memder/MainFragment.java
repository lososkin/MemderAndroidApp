package com.example.memder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;



public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       final View view = inflater.inflate(R.layout.main, container, false);
        final Button mLike = (Button) view.findViewById(R.id.Like);
        final Button mDis = (Button) view.findViewById(R.id.Dis);
        final Map<String, String> data = new HashMap<String, String>();
        SharedPreferences prefs = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        final String tokenFromStorage = prefs.getString("token", "Token not found");
        final ImageView imageView = view.findViewById(R.id.image);
        System.out.println(imageView);
        imageView.setImageResource(R.drawable.downloadmem);
        mLike.setEnabled(false);
        mDis.setEnabled(false);
        new DownloadImageTask((ImageView) view.findViewById(R.id.image),mLike,mDis)
                .execute(tokenFromStorage,"0");


        mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLike.setEnabled(false);
                mDis.setEnabled(false);
                imageView.setImageResource(R.drawable.downloadmem);
                new DownloadImageTask((ImageView) view.findViewById(R.id.image),mLike,mDis)
                        .execute(tokenFromStorage,"1");

            }
        });

        mDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLike.setEnabled(false);
                mDis.setEnabled(false);
                imageView.setImageResource(R.drawable.downloadmem);
                new DownloadImageTask((ImageView) view.findViewById(R.id.image),mLike,mDis)
                        .execute(tokenFromStorage,"-1");
            }
        });
//        Button mMenu = (Button) view.findViewById(R.id.Menu);
        return view;
    }

}
