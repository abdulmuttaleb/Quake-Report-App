package com.isaiko.quakereportapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Quake_MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = Quake_MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quake_main);

        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        earthquakes.add(new Earthquake("7.2", "San Francisco", "Feb 2, 2016"));
        earthquakes.add(new Earthquake("6.1", "London", "Jul 20, 2015"));
        earthquakes.add(new Earthquake("3.9", "Tokyo", "Nov 10, 2014"));
        earthquakes.add(new Earthquake("5.4", "Mexico", "Feb 2, 2016"));
        earthquakes.add(new Earthquake("2.8", "Moscow", "Feb 2, 2016"));
        earthquakes.add(new Earthquake("4.9", "Rio de Janeiro", "Feb 2, 2016"));
        earthquakes.add(new Earthquake("1.9", "Paris", "Feb 2, 2016"));


        ListView earthquakesView = (ListView) findViewById(R.id.list);

        EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);

        earthquakesView.setAdapter(adapter);
    }
}
