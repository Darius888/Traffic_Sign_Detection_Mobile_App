package com.example.traffic_sign_detection.stream_and_data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import androidx.fragment.app.Fragment;

import com.example.traffic_sign_detection.R;
import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.longdo.mjpegviewer.MjpegView;
//import com.github.niqdev.mjpeg.MjpegView;

import java.util.function.DoubleToIntFunction;

public class StreamFragment extends Fragment {

    private MjpegView mjpegView;
    private View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initViews(inflater, container);

        if (checkURL("http://192.168.0.174:8090"))
        {
            loadStream("http://192.168.0.174:8090");
        } else
        {
            return null;
        }

        return root;
    }

    private void initViews(LayoutInflater inflater, ViewGroup container)
    {
        root = inflater.inflate(R.layout.fragment_stream, container, false);
        //mjpegView = root.findViewById(R.id.stream_view);
        mjpegView = root.findViewById(R.id.mjpegview);


    }

    private void loadStream(String URL)
    {
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



    private boolean checkURL(String URL)
    {
        System.out.println("SASLYKAS" + URLUtil.isValidUrl(URL));
        return URLUtil.isValidUrl(URL);
    }


}
