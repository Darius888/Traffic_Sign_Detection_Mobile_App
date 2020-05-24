package com.example.traffic_sign_detection.stream_and_data;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.traffic_sign_detection.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataFragment extends Fragment {

    private ArrayList<String> predictionClassNames = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesBusStop = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesCannotPark = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesCannotStop = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesCrosswalk = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesMainRoad = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesParkingSpot = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesRoadWork = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesStopSigns = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesSpeed70 = new ArrayList<>();

    private View root;

    private TextView colorz;
    private TextView predictionName;
    private TextView avgPredictionProbability;

    private TableLayout tableLayout;

    private Disposable disposable;
    private Disposable disposable2;
    private RetrofitInterface service;

    private BarDataSet dataSet;
    private BarChart barChart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_data, container, false);
        setRetainInstance(true);

        initViews(root);

        disposable = Observable.interval(1, 10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getData, this::onError);

        MaterialButton materialButton = root.findViewById(R.id.refreshChart);
        materialButton.setOnClickListener(v ->
                getData(null));


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://78.56.203.39:8070")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(RetrofitInterface.class);

        return root;
    }

    private void getData(Long along) {

        Observable<List<PredictionModel>> observable = service.getAllPredictionModels();
        observable.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::handleResults, this::handleError);


    }

    private void initViews(View root) {

        tableLayout = root.findViewById(R.id.tableLayout);
        barChart = root.findViewById(R.id.barchart);

    }

    private void onError(Throwable throwable) {
        Toast.makeText(root.getContext(), "OnError in Observable Timer",
                Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void handleResults(List<PredictionModel> url) {
        tableLayout.removeAllViews();
        barChart.invalidate();
        barChart.clear();

        if (url != null) {
            for (int i = 0; i < url.size(); i++) {


                predictionClassNames.add(url.get(i).getPredictionClassName());

                switch (url.get(i).getPredictionClassName()) {
                    case "bus_stop":
                        predictionProbabilitiesBusStop.add(Float.valueOf(url.get(i).getPredictionProbability()));
                        break;
                    case "cannot_park":
                        predictionProbabilitiesCannotPark.add(Float.valueOf(url.get(i).getPredictionProbability()));
                        break;
                    case "cannot_stop":
                        predictionProbabilitiesCannotStop.add(Float.valueOf(url.get(i).getPredictionProbability()));
                        break;
                    case "crosswalk":
                        predictionProbabilitiesCrosswalk.add(Float.valueOf(url.get(i).getPredictionProbability()));
                        break;
                    case "main_road":
                        predictionProbabilitiesMainRoad.add(Float.valueOf(url.get(i).getPredictionProbability()));
                        break;
                    case "parking_spot":
                        predictionProbabilitiesParkingSpot.add(Float.valueOf(url.get(i).getPredictionProbability()));
                        break;
                    case "road_work":
                        predictionProbabilitiesRoadWork.add(Float.valueOf(url.get(i).getPredictionProbability()));
                        break;
                    case "stop_signs":
                        predictionProbabilitiesStopSigns.add(Float.valueOf(url.get(i).getPredictionProbability()));
                        break;
                    case "speed_70":
                        predictionProbabilitiesSpeed70.add(Float.valueOf(url.get(i).getPredictionProbability()));
                        break;
                }

                System.out.println("VAS IS DAS" + url);

            }

            float busStopAverageProbability = average(predictionProbabilitiesBusStop);
            float cannotParkAverageProbability = average(predictionProbabilitiesCannotPark);
            float cannotStopAverageProbability = average(predictionProbabilitiesCannotStop);
            float crosswalkAverageProbability = average(predictionProbabilitiesCrosswalk);
            float mainRoadAverageProbability = average(predictionProbabilitiesMainRoad);
            float parkingSpotAverageProbability = average(predictionProbabilitiesParkingSpot);
            float roadWorkAverageProbability = average(predictionProbabilitiesRoadWork);
            float stopSignsAverageProbability = average(predictionProbabilitiesStopSigns);
            float speed70AverageProbability = average(predictionProbabilitiesSpeed70);

            ArrayList<Float> averageProbabilities = new ArrayList<>();

            if (!Float.isNaN(busStopAverageProbability)) {
                averageProbabilities.add(busStopAverageProbability);
            }
            if (!Float.isNaN(cannotParkAverageProbability)) {
                averageProbabilities.add(cannotParkAverageProbability);
            }
            if (!Float.isNaN(cannotStopAverageProbability)) {
                averageProbabilities.add(cannotStopAverageProbability);
            }
            if (!Float.isNaN(crosswalkAverageProbability)) {
                averageProbabilities.add(crosswalkAverageProbability);
            }
            if (!Float.isNaN(mainRoadAverageProbability)) {
                averageProbabilities.add(mainRoadAverageProbability);
            }
            if (!Float.isNaN(parkingSpotAverageProbability)) {
                averageProbabilities.add(parkingSpotAverageProbability);
            }
            if (!Float.isNaN(roadWorkAverageProbability)) {
                averageProbabilities.add(roadWorkAverageProbability);
            }
            if (!Float.isNaN(stopSignsAverageProbability)) {
                averageProbabilities.add(stopSignsAverageProbability);
            }
            if (!Float.isNaN(speed70AverageProbability)) {
                averageProbabilities.add(speed70AverageProbability);
            }
            System.out.println("VAAAAAAAAAA :" + cannotParkAverageProbability);

            List<String> listWithoutDuplicates = predictionClassNames.stream()
                    .distinct()
                    .collect(Collectors.toList());


            ArrayList<BarEntry> wut = new ArrayList<>();

            for (int i = 0; i < averageProbabilities.size(); i++) {
                wut.add(new BarEntry(i, averageProbabilities.get(i)));

            }

            ArrayList<String> xAxisLabel = new ArrayList<String>(listWithoutDuplicates);

            System.out.println("TIKRINIMAS: " + xAxisLabel);


            int[] colors = new int[10];
            int counter = 0;

            for (int color : ColorTemplate.JOYFUL_COLORS
            ) {
                colors[counter] = color;
                counter++;
            }

            for (int color : ColorTemplate.MATERIAL_COLORS
            ) {
                colors[counter] = color;
                counter++;
            }


            BarDataSet dataSet = new BarDataSet(wut, xAxisLabel.get(0));
            dataSet.setValueTextSize(5);
            dataSet.setDrawValues(false);
            dataSet.setValueFormatter(new PercentFormatter());

            dataSet.setColors(colors);
            BarData data = new BarData(dataSet);
            barChart.setData(data);
            barChart.setDescription(null);
            barChart.setFitBars(true);
            barChart.fitScreen();
            barChart.getLegend().setEnabled(false);

            barChart.invalidate();

            System.out.println("PO ADDITION :" + xAxisLabel);

            YAxis yAxisRight = barChart.getAxisRight();
            yAxisRight.setEnabled(false);

            YAxis yAxisLeft = barChart.getAxisLeft();
            yAxisLeft.setTextColor(Color.WHITE);
            yAxisLeft.setAxisMaximum(0.0f);
            yAxisLeft.setAxisMaximum(1.0f);
            yAxisLeft.setStartAtZero(true);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setEnabled(false);

            for (int i = 0; i < colors.length; i++) {

                TableRow row = new TableRow(root.getContext());
                TableRow.LayoutParams lp = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1
                );

                row.setLayoutParams(lp);
                row.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.table_border));
                row.setBackgroundColor(colors[i]);

                TextView predictionName = new TextView(root.getContext());
                predictionName.setText(xAxisLabel.get(i));
                predictionName.setTextColor(Color.BLACK);
                predictionName.setGravity(Gravity.CENTER);
                predictionName.setLayoutParams(lp);

                TextView probability = new TextView(root.getContext());
                probability.setText(String.valueOf(averageProbabilities.get(i)));
                probability.setTextColor(Color.BLACK);
                probability.setGravity(Gravity.CENTER);
                probability.setLayoutParams(lp);


                row.addView(predictionName);
                row.addView(probability);

                tableLayout.addView(row, i);
            }


        } else {
            Toast.makeText(root.getContext(), "NO RESULTS FOUND",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(Throwable t) {

        //Add your error here.
    }


    private float average(ArrayList<Float> x) {
        float sum = 0;
        for (float aX : x) sum += aX;
        return (sum / x.size());
    }


}
