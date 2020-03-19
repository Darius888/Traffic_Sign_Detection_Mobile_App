package com.example.traffic_sign_detection.stream_and_data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.traffic_sign_detection.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.longdo.mjpegviewer.MjpegView;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.github.niqdev.mjpeg.DisplayMode;
//import com.github.niqdev.mjpeg.Mjpeg;
//import com.github.niqdev.mjpeg.MjpegView;

public class StreamFragment extends Fragment {

    private MjpegView mjpegView;
    private ImageView imageView;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initViews(inflater, container);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.126:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitStreamService service = retrofit.create(RetrofitStreamService.class);

        Call<JsonObject> predictionModels = service.getLastPredictionImageURL();

        predictionModels.enqueue(new Callback<JsonObject>() {

                                     @SneakyThrows
                                     @Override
                                     public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                         System.out.println("VA CIA VA" + response.body());

                                         File file = new File("/home/darius/Desktop/a.png");

                                         URI fileUri = file.toURI();
                                         System.out.println("URI:" + fileUri);

                                         URL fileUrl = file.toURI().toURL();
                                         System.out.println("URL:" + fileUrl);

//                                         JsonElement temp = response.body().get("url");
//                                         String opa = "http://192.168.58.11:8080/home/darius/Desktop/a.png";
//                                         System.out.println("OPAPAPAPA" + opa);
//                                         Glide.with(getContext())
//                                                 .load(fileUri) // Uri of the picture
//                                                 .into(imageView);
//                                         URL urlImage = new URL(
//                                                 "http://192.168.58.11:8080/home/darius/Desktop/a.png");
//                                         HttpURLConnection connection = (HttpURLConnection) urlImage
//                                                 .openConnection();
//                                         InputStream inputStream = connection.getInputStream();
//                                         Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                                         imageView.setImageBitmap(bitmap);


//                                         Glide.with(getContext())
//                                                 .load(new File(Uri.parse(temp.toString()).getPath())) // Uri of the picture
//                                                 .into(imageView);

                                     }

                                     @Override
                                     public void onFailure(Call<JsonObject> call, Throwable t) {
                                         System.out.println("KAS CIA DABAR NU" + t);
                                     }
                                 });


        if (checkURL("http://192.168.1.126:8090")) {
            loadStream("http://192.168.1.126:8090");
        } else {
            return null;
        }

        return root;
    }

    private void initViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.fragment_stream, container, false);
        //mjpegView = root.findViewById(R.id.stream_view);
        mjpegView = root.findViewById(R.id.mjpegview);
        imageView = root.findViewById(R.id.lastdetectionimage);


    }

    private void loadStream(String URL) {
        mjpegView.setMode(MjpegView.MODE_FIT_WIDTH);
        mjpegView.setAdjustHeight(true);
        mjpegView.setUrl(URL);
        mjpegView.startStream();

//when user leaves application
        //mjpegView.stopStream();
//
//        int TIMEOUT = 1; //seconds
//
//        Mjpeg.newInstance()
//                .credential("USERNAME", "PASSWORD")
//                .open(URL, TIMEOUT)
//                .subscribe(inputStream -> {
//                    mjpegView.setSource(inputStream);
//                    mjpegView.setDisplayMode(DisplayMode.BEST_FIT);
//                    mjpegView.showFps(true);
//                });
    }


    private boolean checkURL(String URL) {
        System.out.println("SASLYKAS" + URLUtil.isValidUrl(URL));
        return URLUtil.isValidUrl(URL);
    }

}
