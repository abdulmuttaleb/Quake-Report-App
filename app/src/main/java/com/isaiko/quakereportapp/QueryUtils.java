package com.isaiko.quakereportapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Sample JSON response for a USGS query */

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */

    private static URL createURL(String StringUrl){
        URL url = null;
        try {
            url = new URL(StringUrl);
        }catch (MalformedURLException e){
            Log.e("Query Utils", "Malformed URL Exception", e);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{

        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream= null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        }catch (IOException e){

        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{

        StringBuilder outputStream = new StringBuilder();
        if(inputStream != null){
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamReader);

            String line = reader.readLine();
            while(line != null){
                outputStream.append(line);
                line = reader.readLine();
            }
        }
        return outputStream.toString();
    }

    private static List<Earthquake> extractFeatureFromJson(String earthquakeJson){

        if (TextUtils.isEmpty(earthquakeJson)) {
            return null;
        }

        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            JSONObject mJSONObject = new JSONObject(earthquakeJson);
            JSONArray jEarthquakes = mJSONObject.getJSONArray("features");

            for(int i = 0; i < jEarthquakes.length();i++){
                JSONObject obj = jEarthquakes.getJSONObject(i);
                JSONObject prop = obj.getJSONObject("properties");
                Double mag = prop.getDouble("mag");
                String loc = prop.getString("place");
                Long time = prop.getLong("time");
                String url = prop.getString("url");
                Earthquake eq = new Earthquake(mag,loc,time,url);
                earthquakes.add(eq);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    public static List<Earthquake> fetchEarthquakeData(String requestURL){
        URL url = createURL(requestURL);

        String jsonResponse = null;

        try{
            jsonResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e("QUERY UTILS", "Problem making the HTTP request.", e);
        }

        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);

        return earthquakes;
    }
}