package com.isaiko.quakereportapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Quake_MainActivity extends AppCompatActivity {

    private final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    public static final String LOG_TAG = Quake_MainActivity.class.getName();

    private EarthquakeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quake_main);

        ListView earthquakesView = (ListView) findViewById(R.id.list);

        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        earthquakesView.setAdapter(mAdapter);

        // Add on click listener that leads to the web page of the earthquake
        earthquakesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Earthquake quake = mAdapter.getItem(i);

                String url = quake.getmURL();

                openURL(url);
            }
        });

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);
    }

    private void openURL(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>>{
        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            mAdapter.clear();

            if(earthquakes != null || !earthquakes.isEmpty()){
                mAdapter.addAll(earthquakes);
            }
        }

        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            if(urls.length < 1 || urls[0] == null){
                return null;
            }

            List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }
    }
}
