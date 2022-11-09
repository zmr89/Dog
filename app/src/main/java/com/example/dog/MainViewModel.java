package com.example.dog;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {
    private static final String BASE_URL = "https://dog.ceo/api/breeds/image/random";
    private static final String KEY_MASSAGE = "message";
    private static final String KEY_STATUS = "status";
    private static final String TAG = "MainViewModel";

    private MutableLiveData<DogImage> dogImageMLD = new MutableLiveData();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadDogImage(){
        Disposable disposable = loadDogImageRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DogImage>() {
                    @Override
                    public void accept(DogImage dogImage) throws Throwable {
                        dogImageMLD.setValue(dogImage);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "Error: " + throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);
    }

    public LiveData<DogImage> getDogImageMLD() {
        return dogImageMLD;
    }

    public Single<DogImage> loadDogImageRx(){
        return Single.fromCallable(new Callable<DogImage>() {
            @Override
            public DogImage call() throws Exception {

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
                String message = jsonObject.getString(KEY_MASSAGE);
                String status = jsonObject.getString(KEY_STATUS);
                DogImage dogImage = new DogImage(message, status);

                return dogImage;
            }
        });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
