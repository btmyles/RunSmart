package com.cs2063.runsmart.util;

import android.content.Context;

import com.cs2063.runsmart.model.HistoryData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JsonUtils {

    private static final String HISTORY_JSON_FILE = "History.json";

    private static final String KEY_HISTORY = "history";
    private static final String KEY_AVERAGE_PACE = "average_pace";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_START_TIME = "start_time";

    private ArrayList<HistoryData>  historyArray;

    // Initializer to read our data source (JSON file) into an array of course objects
    public JsonUtils(Context context) {
        processJSON(context);
    }

    private void processJSON(Context context) {
        historyArray = new ArrayList<>();

        try {
            // Create a JSON Object from file contents String
            JSONObject jsonObject = new JSONObject(loadJSONFromAssets(context));

            // Create a JSON Array from the JSON Object
            // This array is the "courses" array mentioned in the lab write-up
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_HISTORY);

            for (int i=0; i < jsonArray.length(); i++) {
                // Create a JSON Object from individual JSON Array element
                JSONObject elementObject = jsonArray.getJSONObject(i);

                JSONArray Jsonlatitude = elementObject.getJSONArray(KEY_LATITUDE);
                JSONArray Jsonlongitude = elementObject.getJSONArray(KEY_LONGITUDE);
                double[] latitude = new double[Jsonlatitude.length()];
                double[] longitude = new double[Jsonlongitude.length()];

                for (int j=0; j<Jsonlatitude.length(); j++) {
                    latitude[j] = Jsonlatitude.getDouble(j);
                    longitude[j] = Jsonlongitude.getDouble(j);
                }

                // Get data from individual JSON Object
                HistoryData historyData = new HistoryData.Builder(elementObject.getLong(KEY_START_TIME),
                        elementObject.getLong(KEY_END_TIME),
                        latitude,longitude)
                        .build();

                // Add new Course to courses ArrayList
                historyArray.add(historyData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAssets(Context context) {
        try {
            InputStream is = context.getAssets().open(HISTORY_JSON_FILE);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    // Getter method for courses ArrayList
    public ArrayList<HistoryData> getHistoryData() {return historyArray;}
}
