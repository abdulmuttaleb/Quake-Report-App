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

        ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();

        ListView earthquakesView = (ListView) findViewById(R.id.list);

        EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);

        earthquakesView.setAdapter(adapter);
    }
}
