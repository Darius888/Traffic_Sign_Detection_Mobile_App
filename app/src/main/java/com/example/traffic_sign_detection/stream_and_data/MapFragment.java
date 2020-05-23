package com.example.traffic_sign_detection.stream_and_data;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.traffic_sign_detection.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapFragment extends Fragment implements OnMapReadyCallback {


    View root;

    MapView mapView;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;

    String temp="";

    Disposable disposable;

    RetrofitStreamService service;

    ArrayList<MarkerModel> markers = new ArrayList<MarkerModel>();
    ArrayList<String> predictionNames = new ArrayList<>();
    ArrayList<Float> predictionProbabilities = new ArrayList<>();

    Double longitude = 0.0;
    Double latitude = 0.0;

    String sign = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_map, container, false);
        setRetainInstance(true);
        System.out.println("Aleliuja ");

        mLocationRequest = new LocationRequest();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(root.getContext());

        mapView = root.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        predictionNames.add("tmp");
        predictionProbabilities.add((float) 1.1);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://78.56.203.39:8070")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(RetrofitStreamService.class);

        disposable = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getLastPredictionData, this::onError);

        return root;
    }


    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                switch (sign) {
                    case "bus_stop":
                        markers.add(new MarkerModel(location.getLatitude(), location.getLongitude(), "Current position", R.drawable.ic_bus_stop));
                        break;
                    case "cannot_park":
                        markers.add(new MarkerModel(location.getLatitude(), location.getLongitude(), "Current position", R.drawable.ic_no_thc));
                        break;
                    case "cannot_stop":
                        markers.add(new MarkerModel(location.getLatitude(), location.getLongitude(), "Current position", R.drawable.ic_cannot_park));
                        break;
                    case "road_work":
                        markers.add(new MarkerModel(location.getLatitude(), location.getLongitude(), "Current position", R.drawable.ic_road_work));
                        break;
                    case "speed_70":
                        markers.add(new MarkerModel(location.getLatitude(), location.getLongitude(), "Current position", R.drawable.ic_speed_limit_70_roadsign));
                        break;
                    case "stop_signs":
                        markers.add(new MarkerModel(location.getLatitude(), location.getLongitude(), "Current position", R.drawable.ic_stop_sign));
                        break;
                    case "crosswalk":
                        markers.add(new MarkerModel(location.getLatitude(), location.getLongitude(), "Current position", R.drawable.ic_crosswalk));
                        break;
                    case "parking_spot":
                        markers.add(new MarkerModel(location.getLatitude(), location.getLongitude(), "Current position", R.drawable.ic_parking));
                        break;
                    case "main_road":
                        markers.add(new MarkerModel(location.getLatitude(), location.getLongitude(), "Current position", R.drawable.ic_main_road));
                        break;
//                    case "tmp":
//                        markers.add(new MarkerModel(location.getLatitude(), location.getLongitude(), "Current position", R.drawable.ic_marker));
//                        break;
                }


                latitude = location.getLatitude();
                longitude = location.getLongitude();

                for(int i = 0 ; i < markers.size() ; i++) {

                        createMarker(markers.get(i).getLattitude(), markers.get(i).getLongitude(), markers.get(i).getTitle(), markers.get(i).getDrawable());

                }



                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(15).build();
                    mGoogleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));


            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(root.getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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

            url.getPredictionClassName();
            url.getPredictionProbability();
            url.getTimestamp().substring(0,19).replace("T", " ");


            System.out.println("VZG" + predictionNames);

            System.out.println("KA AS CIA GAVAU" + url.getPredictionClassName());

            if((predictionNames.get(predictionNames.size()-1).equals(url.getPredictionClassName()))) {
                System.out.println("SUTAMPA" );
            } else
            {
                System.out.println("NESUTAMPA" );
                predictionNames.add(url.getPredictionClassName());
                predictionProbabilities.add(Float.valueOf(url.getPredictionProbability()));
                sign=url.getPredictionClassName();

                System.out.println("VA DABAR");
                onMapReady(mGoogleMap);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://78.56.203.39:8070")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                PredictionLocationModel predictionLocationModel = new PredictionLocationModel(latitude,longitude);

                RetrofitInterface service = retrofit.create(RetrofitInterface.class);

                Call<PredictionLocationModel> postLocation = service.postPredictionLocation(predictionLocationModel);

                postLocation.enqueue(new Callback<PredictionLocationModel>() {
                    @SneakyThrows
                    @Override
                    public void onResponse(Call<PredictionLocationModel> call, Response<PredictionLocationModel> response) {

                        if(response.body()==null)
                        {
                            System.out.println("OPA1");
                        } else
                        {
                            System.out.println("OPA2");
                        }

                    }

                    @Override
                    public void onFailure(Call<PredictionLocationModel> call, Throwable t) {

                    }
                });

            }

            System.out.println("VAS IS DAS" + url.getPredictionClassName());


        } else {
            Toast.makeText(root.getContext(), "NO RESULTS FOUND",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(Throwable t) {
        //Add your error here.
    }

    protected Marker createMarker(double latitude, double longitude, String title, @DrawableRes int vectorDrawableResourceId ) {

        return mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .icon(bitmapDescriptorFromVector(root.getContext(), vectorDrawableResourceId)));
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, 65, 65);
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, 65, 65);
        Bitmap bitmap = Bitmap.createBitmap(65, 65, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
