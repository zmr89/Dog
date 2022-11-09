package com.example.dog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ImageView imageViewDog;
    private Button buttonLoadImage;
    private ProgressBar progressBar;

    MainViewModel mainViewModel;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mainViewModel.loadDogImage();

        mainViewModel.getDogImageMLD().observe(this, new Observer<DogImage>() {
            @Override
            public void onChanged(DogImage dogImage) {
                Glide.with(MainActivity.this)
                        .load(dogImage.getMessage())
                        .into(imageViewDog);
                Log.d(TAG, dogImage.toString());
            }
        });


    }

    private void initViews(){
         imageViewDog=findViewById(R.id.imageViewDog);
         buttonLoadImage=findViewById(R.id.buttonLoadImage);
         progressBar=findViewById(R.id.progressBar);
    }


}