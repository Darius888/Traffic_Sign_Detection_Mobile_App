package com.example.traffic_sign_detection.stream_and_data;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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
    private TextView lastPredictionClassName;
    private TextView lastPredictionProbability;
    private TextView lastPredictionTimestamp;

    String temp1;
    String temp2;

    Disposable disposable;

    RetrofitStreamService service;

    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initViews(inflater, container);


        setRetainInstance(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://78.56.203.39:8070")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(RetrofitStreamService.class);

        disposable = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getLastPredictionData, this::onError);


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
        lastPredictionClassName = root.findViewById(R.id.lastPredClassName);
        lastPredictionProbability = root.findViewById(R.id.lastPredProb);
        lastPredictionTimestamp = root.findViewById(R.id.lastPredTimestamp);
    }

    @SuppressLint("CheckResult")
    public void getLastPredictionData(Long aLong){
        Observable<PredictionModel> observable = service.getLastPredictionData();
        observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::handleResults, this::handleError);
    }

    private void onError(Throwable throwable) {
        Toast.makeText(root.getContext(), "OnError in Observable Timer",
                Toast.LENGTH_LONG).show();
    }

    private void handleResults(PredictionModel url) {

        if (url != null) {

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(root.getContext()).load("http://78.56.203.39:8070/predictions/last/image").apply(options).dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(imageView);

            lastPredictionClassName.setText(url.getPredictionClassName());
            lastPredictionProbability.setText(url.getPredictionProbability() + " %");
            lastPredictionTimestamp.setText(url.getTimestamp().substring(0,19).replace("T", " "));

            temp1 = "";
            temp2 = url.getPredictionClassName();

            temp1 = temp2;





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
