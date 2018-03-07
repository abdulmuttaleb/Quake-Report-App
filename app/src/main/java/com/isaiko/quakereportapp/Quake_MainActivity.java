package com.isaiko.quakereportapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Quake_MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = Quake_MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quake_main);

        final ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();

        ListView earthquakesView = (ListView) findViewById(R.id.list);

        EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);

        earthquakesView.setAdapter(adapter);

        // Add on click listener that leads to the web page of the earthquake
        earthquakesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Earthquake quake = earthquakes.get(i);

                String url = quake.getmURL();

                openURL(url);
            }
        });
    }

    private void openURL(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
