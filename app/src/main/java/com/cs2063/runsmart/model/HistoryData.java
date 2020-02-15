package com.cs2063.runsmart.model;

import androidx.annotation.NonNull;

public class HistoryData {
    private long startTime;   // in ms
    private long endTime;
    private long duration;
    private double distance;
    private double[] latitude;
    private double[] longitude;
    private double avgPace;

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

    private void deriveAttributes() {
        // Calculate duration, distance, avg pace
        this.duration = endTime-startTime;
        this.distance = 0; //TODO
        this.avgPace = this.distance/this.duration;
    }
}