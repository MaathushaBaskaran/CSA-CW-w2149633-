package com.mycompany.csaprj.store;

import com.mycompany.csaprj.model.Room;
import com.mycompany.csaprj.model.Sensor;
import com.mycompany.csaprj.model.SensorReading;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    // Our in-memory "tables"
    private static Map<String, Room> rooms = new HashMap<>();
    private static Map<String, Sensor> sensors = new HashMap<>();
    private static Map<String, List<SensorReading>> sensorReadings = new HashMap<>();

    // Getters to access our "tables"
    public static Map<String, Room> getRooms() {
        return rooms;
    }

    public static Map<String, Sensor> getSensors() {
        return sensors;
    }

    public static Map<String, List<SensorReading>> getSensorReadings() {
        return sensorReadings;
    }
}