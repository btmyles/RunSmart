package com.cs2063.runsmart.model;

import androidx.annotation.NonNull;
import com.mapbox.turf.TurfMeasurement;
import com.mapbox.geojson.Point;
import static com.mapbox.turf.TurfConstants.UNIT_KILOMETERS;

public class HistoryData {

    private long startTime;   // in ms
    private long endTime;
    private long duration;
    private double distance;
    private double[] latitude;
    private double[] longitude;
    private double avgPace;

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getDuration() {
        return duration;
    }

    public double getDistance() {
        return distance;
    }

    public double[] getLatitude() {
        return latitude;
    }

    public double[] getLongitude() {
        return longitude;
    }

    public double getAvgPace() {
        return avgPace;
    }

    public static class Builder {
        private long startTime;   // in ms
        private long endTime;
        private double[] latitude;
        private double[] longitude;

        public Builder(@NonNull long startTime,@NonNull long endTime,@NonNull double[] latitude,@NonNull double[] longitude){
            this.startTime = startTime;
            this.endTime = endTime;
            this.latitude = latitude;
            this.longitude = longitude;
        }
        public HistoryData build() { return new HistoryData(this);}
    }

    public HistoryData(Builder builder){
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public void deriveAttributes() {
        // Calculate duration, distance, avg pace
        this.duration = endTime-startTime;
        this.distance=0;
        Point p1, p2;

        if (this.latitude.length > 0) {
            p2 = Point.fromLngLat(longitude[0], latitude[0]);

            for (int i = 0; i < this.latitude.length - 1; i++) {
                p1 = p2;
                p2 = Point.fromLngLat(longitude[i + 1], latitude[i + 1]);

                this.distance += TurfMeasurement.distance(p1, p2, UNIT_KILOMETERS);
            }
        }
        // millis is converted to hrs
        this.avgPace = this.distance/(this.duration / 1000 / 3600);
    }
}