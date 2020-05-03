package com.example.traffic_sign_detection.stream_and_data;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.traffic_sign_detection.R;
import com.longdo.mjpegviewer.MjpegView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class StreamFragment extends Fragment {

    private MjpegView mjpegView;
    private ImageView imageView;
    private View root;
    Disposable disposable;


    ArrayList<Integer> a= new ArrayList<>();

    RetrofitStreamService service;

    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initViews(inflater, container);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://78.56.203.39:8070")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(RetrofitStreamService.class);






        disposable = Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getMaxUrlNumber, this::onError);

        if (checkURL("http://78.56.203.39:8090")) {
            loadStream("http://78.56.203.39:8090");
        } else {
            return null;
        }

        return root;
    }

    private void initViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.fragment_stream, container, false);
        mjpegView = root.findViewById(R.id.mjpegview);
        imageView = root.findViewById(R.id.lastDetection);
    }

    public void getMaxUrlNumber(Long aLong){
        Observable<Integer> observable = service.getLastPredictionImageURL();
        observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::handleResults, this::handleError);

    }

    private void onError(Throwable throwable) {
        Toast.makeText(root.getContext(), "OnError in Observable Timer",
                Toast.LENGTH_LONG).show();
    }


    private void handleResults(Integer url) {


        if (url != null) {

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(root.getContext()).load("http://78.56.203.39:8070/" + url + ".jpg").apply(options).into(imageView);
            System.out.println("VAS IS DAS" + url);


        } else {
            Toast.makeText(root.getContext(), "NO RESULTS FOUND",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(Throwable t) {

        //Add your error here.
    }


    private void loadStream(String URL) {
        mjpegView.setMode(MjpegView.MODE_FIT_WIDTH);
        mjpegView.setAdjustHeight(true);
        mjpegView.setUrl(URL);
        mjpegView.startStream();

    }


    private boolean checkURL(String URL) {
        System.out.println("SASLYKAS" + URLUtil.isValidUrl(URL));
        return URLUtil.isValidUrl(URL);
    }

}
