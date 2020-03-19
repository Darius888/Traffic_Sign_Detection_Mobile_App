package com.example.traffic_sign_detection.stream_and_data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.example.traffic_sign_detection.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataFragment extends Fragment {

    private PieChart pieChart;

    private ArrayList<String> predictionClassNames = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesPerson = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesCellPhone = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesChair = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesBottle = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesCup = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesTvMonitor = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesBackpack = new ArrayList<>();
    private ArrayList<Float> predictionProbabilitiesHandbag = new ArrayList<>();


    private PieChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_data, container, false);

        getAllPredictionModels();


        MaterialButton materialButton = root.findViewById(R.id.refreshChart);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllPredictionModels();
            }
        });

        return root;
    }


    private void getAllPredictionModels()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.126:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface service = retrofit.create(RetrofitInterface.class);

        Call<List<PredictionModel>> predictionModels = service.getAllPredictionModels();

        predictionModels.enqueue(new Callback<List<PredictionModel>>() {

            @SneakyThrows
            @Override
            public void onResponse(@NotNull Call<List<PredictionModel>>  call, @NotNull Response<List<PredictionModel>> response) {
                Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                if(!(response.body() == null))
                {
                    for(int i=0;i<response.body().size();i++)
                    {
                        predictionClassNames.add(response.body().get(i).getPredictionClassName());

                        if(response.body().get(i).getPredictionClassName().equals("person"))
                        {
                            predictionProbabilitiesPerson.add(Float.valueOf(response.body().get(i).getPredictionProbability()));
                        } else if(response.body().get(i).getPredictionClassName().equals("cell phone"))
                        {
                            predictionProbabilitiesCellPhone.add(Float.valueOf(response.body().get(i).getPredictionProbability()));
                        } else if(response.body().get(i).getPredictionClassName().equals("chair"))
                        {
                            predictionProbabilitiesChair.add(Float.valueOf(response.body().get(i).getPredictionProbability()));
                        } else if(response.body().get(i).getPredictionClassName().equals("bottle"))
                        {
                            predictionProbabilitiesBottle.add(Float.valueOf(response.body().get(i).getPredictionProbability()));
                        } else if(response.body().get(i).getPredictionClassName().equals("cup"))
                        {
                            predictionProbabilitiesCup.add(Float.valueOf(response.body().get(i).getPredictionProbability()));
                        } else if(response.body().get(i).getPredictionClassName().equals("tvmonitor"))
                        {
                            predictionProbabilitiesTvMonitor.add(Float.valueOf(response.body().get(i).getPredictionProbability()));
                        } else if(response.body().get(i).getPredictionClassName().equals("backpack"))
                        {
                            predictionProbabilitiesBackpack.add(Float.valueOf(response.body().get(i).getPredictionProbability()));
                        } else if(response.body().get(i).getPredictionClassName().equals("handbag"))
                        {
                            predictionProbabilitiesHandbag.add(Float.valueOf(response.body().get(i).getPredictionProbability()));
                        }
                }
                }


                float personAverage = average(predictionProbabilitiesPerson);
                float cellPhoneAverage = average(predictionProbabilitiesCellPhone);
                float chairAverage = average(predictionProbabilitiesChair);
                float bottleAverage = average(predictionProbabilitiesBottle);
                float cupAverage = average(predictionProbabilitiesCup);
                float tvMonitorAverage = average(predictionProbabilitiesTvMonitor);
                float backpack = average(predictionProbabilitiesBackpack);
                float handbag = average(predictionProbabilitiesHandbag);

                ArrayList<Float> averageProbabilities = new ArrayList<>();
//                for(int o=0; o < averageProbabilities.size();o++)
//                {
//                    averageProbabilities.add(average())
//                }
                averageProbabilities.add(personAverage);
                averageProbabilities.add(cellPhoneAverage);
                averageProbabilities.add(chairAverage);
                averageProbabilities.add(bottleAverage);
                averageProbabilities.add(cupAverage);
                averageProbabilities.add(tvMonitorAverage);
                averageProbabilities.add(backpack);
                averageProbabilities.add(handbag);

//                Remove duplicates
                Set<String> set1 =  new LinkedHashSet<>(predictionClassNames);
                predictionClassNames.clear();
                predictionClassNames.addAll(set1);
//
//
//                Set<Float> set2 = new HashSet<>(predictionProbabilities);
//                predictionProbabilities.clear();
//                predictionProbabilities.addAll(set2);

                BarChart barChart = root.findViewById(R.id.barchart);

                ArrayList<BarEntry> wut = new ArrayList<>();

                ArrayList<Integer> position = new ArrayList<>();
                for(int p = 0;p<averageProbabilities.size();p++)
                {
                    position.add(p);
                }

                for(int i=0; i < averageProbabilities.size(); i++)
                {
                    wut.add(new BarEntry(position.get(i), averageProbabilities.get(i)));
                }



                BarDataSet dataSet = new BarDataSet(wut,null);
                dataSet.setValueTextSize(20);
                dataSet.setDrawValues(false);
                dataSet.setValueFormatter(new PercentFormatter());
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

                dataSet.setColors(colors);
                BarData data = new BarData(dataSet);
                barChart.setData(data);
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barChart.setDescription(null);
                barChart.setFitBars(true);
                barChart.getLegend().setEnabled(false);
                barChart.invalidate();


                ArrayList<String> xAxisLabel = new ArrayList<>();
                for(int o=0;o<predictionClassNames.size();o++)
                {
                    xAxisLabel.add(predictionClassNames.get(o));
                }

//                    xAxisLabel.add("person");
//                    xAxisLabel.add("cell phone");
//                    xAxisLabel.add("chair");
//                    xAxisLabel.add("bottle");
//                    xAxisLabel.add("cup");
//                    xAxisLabel.add("tvmonitor");
//                    xAxisLabel.add("person");



                YAxis yAxisRight = barChart.getAxisRight();
                yAxisRight.setEnabled(false);

                YAxis yAxisLeft = barChart.getAxisLeft();
                yAxisLeft.setAxisMaximum(0.2f);
                yAxisLeft.setAxisMaximum(0.8f);


                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setLabelCount(predictionClassNames.size());
                xAxis.setValueFormatter(new IndexAxisValueFormatter(getDate(xAxisLabel)));




            }

            @Override
            public void onFailure(@NotNull Call<List<PredictionModel>> call, @NotNull Throwable t) {
                System.out.println("WAZZUPAS" + t);
            }

        });

    }


    private float average(ArrayList<Float> x) {
        float sum = 0;
        for (float aX : x) sum += aX;
        return (sum / x.size());
    }


    private ArrayList<String> getDate(ArrayList<String> xAxisLabel) {

        return new ArrayList<>(xAxisLabel);
    }

}
