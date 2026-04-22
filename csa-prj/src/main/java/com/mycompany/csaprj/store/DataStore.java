package com.mycompany.csaprj.store;

import com.mycompany.csaprj.model.Room;
import com.mycompany.csaprj.model.Sensor;
import com.mycompany.csaprj.model.SensorReading;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataStore {
    // Using thread-safe collections to prevent race conditions from concurrent API requests
    private static Map<String, Room> rooms = new ConcurrentHashMap<>();
    private static Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    private static Map<String, List<SensorReading>> sensorReadings = new ConcurrentHashMap<>();

    public static Map<String, Room> getRooms() { return rooms; }
    public static Map<String, Sensor> getSensors() { return sensors; }
    public static Map<String, List<SensorReading>> getSensorReadings() { return sensorReadings; }
    
    // Helper method to safely get or create a thread-safe list for a sensor's readings
    public static List<SensorReading> getReadingsForSensor(String sensorId) {
        return sensorReadings.computeIfAbsent(sensorId, k -> new CopyOnWriteArrayList<>());
    }
}