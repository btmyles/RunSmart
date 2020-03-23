package com.cs2063.runsmart.util;

import android.content.Context;
import android.util.Log;

import com.cs2063.runsmart.model.HistoryData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class JsonUtils {

    private static final String TAG = "JsonUtils.java";

    private static final String KEY_HISTORY = "history";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_NOTES = "notes";

    private ArrayList<HistoryData>  historyArray;

    // Initializer to read our data source (JSON file) into an array of course objects
    public JsonUtils(Context context) {
        // Write History.json to local files before reading from said local file

        // Read from local files
        processJSON(context);
    }

    private void processJSON(Context context) {
        historyArray = new ArrayList<>();

        try {
            // Create a JSON Object from file contents String
            if(loadJSONFromStorage(context) != null) {
                JSONObject jsonObject = new JSONObject(loadJSONFromStorage(context));

                // Create a JSON Array from the JSON Object
                // This array is the "courses" array mentioned in the lab write-up
                JSONArray jsonArray = jsonObject.getJSONArray(KEY_HISTORY);

                for (int i = 0; i < jsonArray.length(); i++) {
                    // Create a JSON Object from individual JSON Array element
                    JSONObject elementObject = jsonArray.getJSONObject(i);

                    JSONArray Jsonlatitude = elementObject.getJSONArray(KEY_LATITUDE).getJSONArray(0);
                    JSONArray Jsonlongitude = elementObject.getJSONArray(KEY_LONGITUDE).getJSONArray(0);
                    double[] latitude = new double[Jsonlatitude.length()];
                    double[] longitude = new double[Jsonlongitude.length()];

                    for (int j = 0; j < Jsonlatitude.length(); j++) {
                        latitude[j] = Jsonlatitude.getDouble(j);
                        longitude[j] = Jsonlongitude.getDouble(j);
                    }

                    Log.i("JsonUtils.java", "processJSON getting notes key from element object.");
                    String notes = elementObject.getString(KEY_NOTES);

                    // Get data from individual JSON Object
                    HistoryData historyData = new HistoryData.Builder(elementObject.getLong(KEY_START_TIME),
                            elementObject.getLong(KEY_END_TIME), latitude, longitude, notes)
                            .build();

                    historyData.deriveAttributes();
                    // Add new Course to courses ArrayList
                    historyArray.add(historyData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromStorage(Context context) {
        try {
            // read file from local storage
            InputStream is = new FileInputStream(new File(context.getFilesDir(),"history/History.json"));
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

    public String toJSon(Context context, HistoryData history) {
        try {
            history.deriveAttributes();

            // Convert HistoryData Java Object to JSON
            JSONObject mainObject = new JSONObject();
            JSONArray list = new JSONArray();

            JSONObject valuesObject = new JSONObject();
            valuesObject.put("start_time", history.getStartTime());
            valuesObject.put("end_time", history.getEndTime());
            valuesObject.put("duration", history.getDuration());
            valuesObject.put("distance", history.getDistance());
            valuesObject.put("avg_pace", history.getAvgPace());
            JSONArray jsonArrayLat = new JSONArray(Arrays.asList(history.getLatitude()));
            JSONArray jsonArrayLon = new JSONArray(Arrays.asList(history.getLongitude()));
            valuesObject.put("latitude", jsonArrayLat);
            valuesObject.put("longitude", jsonArrayLon);
            Log.i("JsonUtils.java", "toJSon - convert HistoryData notes to JSON");
            valuesObject.put("notes", history.getNotes());

            list.put(valuesObject);

            // convert the rest of the history into JSON and place it in the list, following the new data
            for (int i=0; i<historyArray.size(); i++) {
                valuesObject = new JSONObject();

                valuesObject.put("start_time", historyArray.get(i).getStartTime());
                valuesObject.put("end_time", historyArray.get(i).getEndTime());
                valuesObject.put("duration", historyArray.get(i).getDuration());
                valuesObject.put("distance", historyArray.get(i).getDistance());
                valuesObject.put("avg_pace", historyArray.get(i).getAvgPace());
                jsonArrayLat = new JSONArray(Arrays.asList(historyArray.get(i).getLatitude()));
                jsonArrayLon = new JSONArray(Arrays.asList(historyArray.get(i).getLongitude()));
                valuesObject.put("latitude", jsonArrayLat);
                valuesObject.put("longitude", jsonArrayLon);
                Log.i("JsonUtils.java", "toJSon - putting notes from loop.");
                valuesObject.put("notes", historyArray.get(i).getNotes());

                list.put(valuesObject);
            }

            // Add the new history value to the list
            historyArray.add(0, history);

            mainObject.accumulate("history", list);

            Log.i(TAG, mainObject.toString());

            // Overwrite History.json
            writeToFile(mainObject.toString(), context);

            return mainObject.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Boolean delete(Context context, HistoryData history) {
        try {
            // Convert HistoryData Java Object to JSON
            JSONObject mainObject = new JSONObject();
            JSONArray list = new JSONArray();

            JSONObject valuesObject;
            JSONArray jsonArrayLat;
            JSONArray jsonArrayLon;

            // convert the history array into JSON and place it in the JSON list, except for the data to be deleted
            for (int i=0; i<historyArray.size(); i++) {
                if (history.getStartTime() != historyArray.get(i).getStartTime()) {
                    valuesObject = new JSONObject();

                    valuesObject.put("start_time", historyArray.get(i).getStartTime());
                    valuesObject.put("end_time", historyArray.get(i).getEndTime());
                    valuesObject.put("duration", historyArray.get(i).getDuration());
                    valuesObject.put("distance", historyArray.get(i).getDistance());
                    valuesObject.put("avg_pace", historyArray.get(i).getAvgPace());
                    jsonArrayLat = new JSONArray(Arrays.asList(historyArray.get(i).getLatitude()));
                    jsonArrayLon = new JSONArray(Arrays.asList(historyArray.get(i).getLongitude()));
                    valuesObject.put("latitude", jsonArrayLat);
                    valuesObject.put("longitude", jsonArrayLon);
                    Log.i("JsonUtils.java", "delete - copy notes over in loop");
                    valuesObject.put("notes", historyArray.get(i).getNotes());

                    list.put(valuesObject);
                }
            }

            mainObject.accumulate("history", list);

            Log.i(TAG, "Deleting run at start time: " + history.getStartTime());
            Log.i(TAG, "New JSON data:");
            Log.i(TAG, mainObject.toString());

            // Overwrite History.json
            writeToFile(mainObject.toString(), context);

            // successful write
            return true;

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return false;
    }
    public Boolean updateNotes(Context context, HistoryData history, String notesIn) {
        try {
            // Convert HistoryData Java Object to JSON
            JSONObject mainObject = new JSONObject();
            JSONArray list = new JSONArray();

            JSONObject valuesObject;
            JSONArray jsonArrayLat;
            JSONArray jsonArrayLon;

            // convert the history array into JSON and place it in the JSON list, except for the data to be deleted
            for (int i=0; i<historyArray.size(); i++) {
                if (history.getStartTime() != historyArray.get(i).getStartTime()) {
                    valuesObject = new JSONObject();

                    valuesObject.put("start_time", historyArray.get(i).getStartTime());
                    valuesObject.put("end_time", historyArray.get(i).getEndTime());
                    valuesObject.put("duration", historyArray.get(i).getDuration());
                    valuesObject.put("distance", historyArray.get(i).getDistance());
                    valuesObject.put("avg_pace", historyArray.get(i).getAvgPace());
                    jsonArrayLat = new JSONArray(Arrays.asList(historyArray.get(i).getLatitude()));
                    jsonArrayLon = new JSONArray(Arrays.asList(historyArray.get(i).getLongitude()));
                    valuesObject.put("latitude", jsonArrayLat);
                    valuesObject.put("longitude", jsonArrayLon);
                    Log.i("JsonUtils.java", "delete - copy notes over in loop");
                    valuesObject.put("notes", historyArray.get(i).getNotes());

                    list.put(valuesObject);
                }
                if (history.getStartTime() == historyArray.get(i).getStartTime()) {
                    valuesObject = new JSONObject();

                    valuesObject.put("start_time", historyArray.get(i).getStartTime());
                    valuesObject.put("end_time", historyArray.get(i).getEndTime());
                    valuesObject.put("duration", historyArray.get(i).getDuration());
                    valuesObject.put("distance", historyArray.get(i).getDistance());
                    valuesObject.put("avg_pace", historyArray.get(i).getAvgPace());
                    jsonArrayLat = new JSONArray(Arrays.asList(historyArray.get(i).getLatitude()));
                    jsonArrayLon = new JSONArray(Arrays.asList(historyArray.get(i).getLongitude()));
                    valuesObject.put("latitude", jsonArrayLat);
                    valuesObject.put("longitude", jsonArrayLon);
                    Log.i("JsonUtils.java", "delete - copy notes over in loop");
                    historyArray.get(i).setNotes(notesIn);
                    valuesObject.put("notes", historyArray.get(i).getNotes());

                    list.put(valuesObject);
                }
            }

            mainObject.accumulate("history", list);

            Log.i(TAG, "Modifying notes file for object " + history.getStartTime());
            Log.i(TAG, "New JSON data:");
            Log.i(TAG, mainObject.toString());

            // Overwrite History.json
            writeToFile(mainObject.toString(), context);

            // successful write
            return true;

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    private void writeToFile(String data,Context context) {

        // Open History.json to overwrite it
        File file = new File(context.getFilesDir(),"history");

        // overwrite history.json
        try{
            File gpxfile = new File(file, "History.json");
            FileWriter writer = new FileWriter(gpxfile);
            writer.write(data);
            Log.i("Main", "History.json has been written");
            writer.flush();
            writer.close();

        }catch (Exception e){
            Log.i(TAG, e.toString());
        }
    }

    // Getter method for courses ArrayList
    public ArrayList<HistoryData> getHistoryData() {return historyArray;}
}
