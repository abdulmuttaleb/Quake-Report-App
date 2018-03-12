package com.isaiko.quakereportapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager.LoaderCallbacks;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Quake_MainActivity extends AppCompatActivity  implements LoaderCallbacks<List<Earthquake>>{

    private final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    private static final int EARTHQUAKE_LOADER_ID = 1;

    public static final String LOG_TAG = Quake_MainActivity.class.getName();

    private EarthquakeAdapter mAdapter;

    private TextView mEmptyStateTextView;

    private ProgressBar mProgressBar;

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.d(LOG_TAG, "onCreateLoader");

        String queryURL = formatQueryLink();
        return new EarthquakeLoader(this, queryURL);

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        mAdapter.clear();
        Log.d(LOG_TAG, "Resetting Adapter Content on destroying layout");
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        //hide the progressbar
        mProgressBar.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_earthquakes);

        mAdapter.clear();
        try {
            if (earthquakes != null || !earthquakes.isEmpty()) {
                mAdapter.addAll(earthquakes);
            }
        }catch(NullPointerException e){
            Log.e(LOG_TAG, "Null pointer Exception");
        }
        Log.d(LOG_TAG, "OnLoadFinished");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quake_main);

        //set earthquake view
        ListView earthquakesView = (ListView) findViewById(R.id.list);
        earthquakesView.setEmptyView(findViewById(R.id.empty_view));

        //set Empty state view
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakesView.setEmptyView(mEmptyStateTextView);

        // set progressbar view
        mProgressBar = findViewById(R.id.progress_bar);

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

        // testing for network connectivity
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }else{
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText("Please, check your connection!");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    private void openURL(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private String formatQueryLink(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String minMagnitude = sharedPrefs.getString(getString(
                R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return uriBuilder.toString();
    }
}
