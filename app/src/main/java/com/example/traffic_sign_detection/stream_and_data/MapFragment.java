package com.example.traffic_sign_detection.stream_and_data;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.traffic_sign_detection.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
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


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    SupportMapFragment mapFrag;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private FusedLocationProviderClient mFusedLocationClient;
    String temp = "";

    private Disposable disposable;
    private Disposable disposable2;

    private RetrofitStreamService service;
    private RetrofitStreamService service2;


    private ArrayList<String> predictionNames = new ArrayList<>();
    private ArrayList<Float> predictionProbabilities = new ArrayList<>();

    private Double longitude = 0.0;
    private Double latitude = 0.0;

    private Double gottenLongitude = 0.0;
    private Double gottenLatitude = 0.0;

    private ArrayList<Double> gottenLongitudes = new ArrayList<>();
    private ArrayList<Double> gottenLatitudes = new ArrayList<>();

    private String sign = "";
    private ArrayList<String> gottenSign = new ArrayList<>();


    private Integer count = 0;
    private Integer gottenCount = 0;

    private View root;

    private GoogleMap mGoogleMap;

    private Integer methodCalls = 0;
    private Integer gottenMethodCalls = 0;

    private Integer gpsAccOrNot = 0;
    private Integer iteration = 0;

    private ArrayList<MarkerModel> markers = new ArrayList<MarkerModel>();

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

                if(gpsAccOrNot==1)
                {

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
                        case "tmp":
                            markers.add(new MarkerModel(location.getLatitude(), location.getLongitude(), "Current position", R.drawable.ic_marker));
                            break;

                    }


                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    for (int i = 0; i < markers.size(); i++) {

                        createMarker(markers.get(i).getLattitude(), markers.get(i).getLongitude(), markers.get(i).getTitle(), markers.get(i).getDrawable());

                    }

                    if (count == 0) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(15).build();
                        mGoogleMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                    }

                    count++;
                    methodCalls=1;

                } else if(gpsAccOrNot==0)
                {

                    for(int i=0; i<gottenLongitudes.size() ; i++)
                    {
                        switch (gottenSign.get(i)) {
                            case "bus_stop":
                                markers.add(new MarkerModel(gottenLatitudes.get(i), gottenLongitudes.get(i), "Current position", R.drawable.ic_bus_stop));
                                break;
                            case "cannot_park":
                                markers.add(new MarkerModel(gottenLatitudes.get(i), gottenLongitudes.get(i), "Current position", R.drawable.ic_no_thc));
                                break;
                            case "cannot_stop":
                                markers.add(new MarkerModel(gottenLatitudes.get(i), gottenLongitudes.get(i), "Current position", R.drawable.ic_cannot_park));
                                break;
                            case "road_work":
                                markers.add(new MarkerModel(gottenLatitudes.get(i), gottenLongitudes.get(i), "Current position", R.drawable.ic_road_work));
                                break;
                            case "speed_70":
                                markers.add(new MarkerModel(gottenLatitudes.get(i), gottenLongitudes.get(i), "Current position", R.drawable.ic_speed_limit_70_roadsign));
                                break;
                            case "stop_signs":
                                markers.add(new MarkerModel(gottenLatitudes.get(i), gottenLongitudes.get(i), "Current position", R.drawable.ic_stop_sign));
                                break;
                            case "crosswalk":
                                markers.add(new MarkerModel(gottenLatitudes.get(i), gottenLongitudes.get(i), "Current position", R.drawable.ic_crosswalk));
                                break;
                            case "parking_spot":
                                markers.add(new MarkerModel(gottenLatitudes.get(i), gottenLongitudes.get(i), "Current position", R.drawable.ic_parking));
                                break;
                            case "main_road":
                                markers.add(new MarkerModel(gottenLatitudes.get(i), gottenLongitudes.get(i), "Current position", R.drawable.ic_main_road));
                                break;
                            case "tmp":
                                markers.add(new MarkerModel(gottenLatitudes.get(i), gottenLongitudes.get(i), "Current position", R.drawable.ic_marker));
                                break;
                        }
                    }



//                    System.out.println("O CIA KA GAVAU : " + gottenLongitudes.get(0));
//
//                    for(int i=0; i<gottenLongitudes.size(); i++)
//                    {
//                        markers.add(new MarkerModel(gottenLatitudes.get(i), gottenLongitudes.get(i), "Current position", R.drawable.ic_marker));
//                    }

                    for (int i = 0; i < markers.size(); i++) {

                        createMarker(markers.get(i).getLattitude(), markers.get(i).getLongitude(), markers.get(i).getTitle(), markers.get(i).getDrawable());

                    }

                    if (gottenCount == 0) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(15).build();
                        mGoogleMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                    }

                    gottenCount++;
                    gottenMethodCalls =1;

                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_map, container, false);
        setRetainInstance(true);
        System.out.println("Aleliuja ");
        String user_email = getActivity().getIntent().getStringExtra("user_email");
        System.out.println("ZAZ" + user_email);





        if(user_email.equals("gps@gps.lt"))
        {
            gpsAccOrNot=1;

            mLocationRequest = new LocationRequest();
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(100);
            mLocationRequest.setFastestInterval(100);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(root.getContext());

            MapView mapView = root.findViewById(R.id.map);
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
        } else
        {
            gpsAccOrNot=0;

            mLocationRequest = new LocationRequest();
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(100);
            mLocationRequest.setFastestInterval(100);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(root.getContext());

            MapView mapView = root.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);


            predictionNames.add("tmp");
            predictionProbabilities.add((float) 1.1);

            gottenSign.add("tmp");
            gottenLatitudes.add(gottenLongitude);
            gottenLongitudes.add(gottenLatitude);



            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://78.56.203.39:8070")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            service2 = retrofit.create(RetrofitStreamService.class);

            disposable2 = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::getLastLocation, this::onErrorLocation);

        }



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
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

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
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
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
    public void getLastPredictionData(Long aLong) {


        if(predictionNames.get(0).equals("tmp") && methodCalls !=1)
        {
            sign = "tmp";
            System.out.println("DYDIS : " + predictionNames.size());
            onMapReady(mGoogleMap);
        }

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
            url.getTimestamp().substring(0, 19).replace("T", " ");


            if ((predictionNames.get(predictionNames.size() - 1).equals(url.getPredictionClassName()))) {

                System.out.println("SUTAMPA");
            } else {

                System.out.println("NESUTAMPA");
                predictionNames.add(url.getPredictionClassName());
                predictionProbabilities.add(Float.valueOf(url.getPredictionProbability()));
                sign = url.getPredictionClassName();

                System.out.println("VA DABAR");

                onMapReady(mGoogleMap);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://78.56.203.39:8070")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                PredictionLocationModel predictionLocationModel = new PredictionLocationModel(longitude, latitude, url.getPredictionClassName(), url.getPredictionProbability());

                RetrofitInterface service = retrofit.create(RetrofitInterface.class);

                Call<PredictionLocationModel> postLocation = service.postPredictionLocation(predictionLocationModel);

                postLocation.enqueue(new Callback<PredictionLocationModel>() {
                    @SneakyThrows
                    @Override
                    public void onResponse(Call<PredictionLocationModel> call, Response<PredictionLocationModel> response) {

                        if (response.body() == null) {
                            System.out.println("OPA1");
                        } else {
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

            System.out.println("NO RESULTS FOUND");
        }


    }

    private void handleError(Throwable t) {
        //Add your error here.
    }




    // ------------ VA NUO CIA -------------------------

    @SuppressLint("CheckResult")
    public void getLastLocation(Long aLong) {

        Observable<List<PredictionLocationModel>> observable = service2.getPredictionLocations();
        observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::handleResultsLocation, this::handleErrorLocation);
    }

    private void onErrorLocation(Throwable throwable) {
        Toast.makeText(root.getContext(), "OnError in Observable Timer",
                Toast.LENGTH_LONG).show();
    }


    private void handleResultsLocation(List<PredictionLocationModel> url) {

        if (url != null) {



            gottenLongitude = url.get(url.size()-1).getLng();
            gottenLatitude = url.get(url.size()-1).getLat();


             if((gottenLatitudes.get(gottenLatitudes.size() - 1).equals(url.get(url.size()-1).getLat())) && (gottenLongitudes.get(gottenLongitudes.size() - 1).equals(url.get(url.size()-1).getLng())))
             {
                 System.out.println("SUTAMPA KOORDINATES");
             } else
             {
                 System.out.println("NESUTAMPA KOORDINATES");

                 System.out.println("KA GAVAU" + gottenLongitude);

                 gottenLatitudes.add(gottenLatitude);
                 gottenLongitudes.add(gottenLongitude);

                 gottenSign.add(url.get(url.size() - 1).getPredictionClassName());

                 onMapReady(mGoogleMap);

             }

        } else {
            Toast.makeText(root.getContext(), "NO RESULTS FOUND",
                    Toast.LENGTH_LONG).show();

            System.out.println("NO RESULTS FOUND");
        }


    }

    private void handleErrorLocation(Throwable t) {
        //Add your error here.
        System.out.println("ERRORAS" + t.toString());

    }


    protected Marker createMarker(double latitude, double longitude, String title, @DrawableRes int vectorDrawableResourceId) {

        MarkerOptions a = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .icon(bitmapDescriptorFromVector(root.getContext(), vectorDrawableResourceId));

        Marker m = mGoogleMap.addMarker(a);

        m.setPosition(new LatLng(latitude, longitude));

//        return mGoogleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(latitude, longitude))
//                .anchor(0.5f, 0.5f)
//                .title(title)
//                .icon(bitmapDescriptorFromVector(root.getContext(), vectorDrawableResourceId)));

        return m;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, 80, 80);
//        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
//        vectorDrawable.setBounds(40, 20, 65, 65);
        Bitmap bitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
//        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}
