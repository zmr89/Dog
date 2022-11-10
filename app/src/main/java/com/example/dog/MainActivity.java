package com.example.dog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    private ImageView imageViewDog;
    private Button buttonLoadImage;
    private ProgressBar progressBar;

    MainViewModel mainViewModel;

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
            }
        });

        mainViewModel.getIsLoadingMLD().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoad) {
                if (isLoad){
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        mainViewModel.getIsErrorLoadingMLD().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isErrorLoading) {
                if (isErrorLoading){
                    Toast.makeText(MainActivity.this, R.string.error_loading, Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewModel.loadDogImage();
            }
        });


    }

    private void initViews(){
         imageViewDog=findViewById(R.id.imageViewDog);
         buttonLoadImage=findViewById(R.id.buttonLoadImage);
         progressBar=findViewById(R.id.progressBar);
    }


}