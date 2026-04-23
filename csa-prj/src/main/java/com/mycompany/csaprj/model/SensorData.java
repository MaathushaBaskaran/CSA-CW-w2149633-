package com.mycompany.csaprj.model;

import java.time.LocalDateTime;

public class SensorData {
    private double value;
    private LocalDateTime timestamp;

    public SensorData() {
        this.timestamp = LocalDateTime.now(); // Automatically timestamp the reading
    }

    public SensorData(double value) {
        this.value = value;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}