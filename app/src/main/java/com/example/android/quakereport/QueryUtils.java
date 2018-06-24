package com.example.android.quakereport;

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

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {
    private final static String LOG_TAG = EarthquakeActivity.class.getSimpleName();
    private static String urlString = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2018-01-01&endtime=2018-06-22&minmag=6&limit=10";
    private QueryUtils() {
    }

    public static ArrayList<Earthquake> extractEarthquakes() {
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        makeHTTPRequest(urlString, earthquakes);
        return earthquakes;
    }

    private static void makeHTTPRequest(String stringUrl, ArrayList<Earthquake> earthquakes) {
        String jsonResponse = "";
        URL url = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            url = createUrl(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readInputStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Response Code Error: " + urlConnection.getErrorStream());
            }
            createJSONProperties(jsonResponse, earthquakes);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error: Possible bad URL", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error: Possible bad connection", e);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Error: Problem opening connection", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error: Problem closing connection", e);
                }
            }
        }
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error reading InputStream ", e);
            }
        } else {
            Log.i(LOG_TAG, "inputStream is empty");
        }
        return output.toString();
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private static ArrayList<Earthquake> createJSONProperties(String jsonResponse, ArrayList<Earthquake> earthquakes) {
        if (jsonResponse.equals("") || jsonResponse.equals(null)) {
            Log.e(LOG_TAG, "Error: No JSON Response");
            return null;
        }
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray rootFeatures = root.getJSONArray("features");
            int numberOfEvents = rootFeatures.length();
            // (mag, location, time)
            for (int i = 0; i < numberOfEvents; i++) {
                JSONObject currentEvent = rootFeatures.getJSONObject(i);
                JSONObject properties = currentEvent.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                String propertyUrl = properties.getString("url");
                earthquakes.add(new Earthquake(mag, location, time, propertyUrl));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }
}