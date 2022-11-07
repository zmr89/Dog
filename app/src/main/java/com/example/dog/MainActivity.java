package com.example.dog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

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

    private static final String BASE_URL = "https://dog.ceo/api/breeds/image/random";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadDogImage();
    }

    private void loadDogImage(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
//Создаем URL для работы сос сылкой
                    URL loadURL = new URL(BASE_URL);
//Открываем подключение с помощью метода openConnection()
                    HttpURLConnection httpURLConnection = (HttpURLConnection) loadURL.openConnection();
//Считываем поток побайтово с помощью InputStream
                    InputStream inputStream = httpURLConnection.getInputStream();
//Преобразуем поток байтов в поток символов с помощью InputStreamReader
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//тобы не читать каждый символ по отдельности, а сразу всю строку целиком испоьзуем BufferedReader
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder data = new StringBuilder();
                    String result;

                    do {
                        result = bufferedReader.readLine();
                        data.append(result);
                    } while (result != null);

                    JSONObject jsonObject = new JSONObject(data.toString());
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    DogImage dogImage = new DogImage(message, status);


                    Log.d("MainActivity.java", dogImage.toString());

                } catch (Exception e) {
                    Log.d("MainActivity.java",e.getMessage());
                }

            }
        });

        thread.start();
    }
}