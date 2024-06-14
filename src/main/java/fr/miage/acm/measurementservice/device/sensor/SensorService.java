package fr.miage.acm.measurementservice.device.sensor;

import fr.miage.acm.measurementservice.api.ApiSensor;
import fr.miage.acm.measurementservice.client.ManagementServiceClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SensorService {


    private final ManagementServiceClient managementServiceClient;

    public SensorService(ManagementServiceClient managementServiceClient) {
        this.managementServiceClient = managementServiceClient;
    }

    public List<Sensor> findAll() {
        List<ApiSensor> apiSensors = managementServiceClient.getAllSensors();
        System.out.println(apiSensors);
        // convert ApiSensor to Sensor with for each loop
        List<Sensor> sensors = new ArrayList<>();
        for (ApiSensor apiSensor : apiSensors) {
            sensors.add(new Sensor(apiSensor));
        }
        return sensors;
    }

    public Sensor findById(UUID sensorId) {
        Optional<ApiSensor> optionalApiSensor = managementServiceClient.getSensorById(sensorId);
        if (optionalApiSensor.isPresent()) {
            ApiSensor apiSensor = optionalApiSensor.get();
            return new Sensor(apiSensor);
        }
        return null;
    }


    public void updateMeasures(UUID sensorId, float newTemperature, float newHumidity) {
        managementServiceClient.updateSensorMeasure(sensorId, newTemperature, newHumidity);
    }
}
