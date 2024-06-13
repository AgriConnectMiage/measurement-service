package fr.miage.acm.measurementservice.device.sensor;

import fr.miage.acm.measurementservice.client.ManagementServiceClient;
import org.springframework.stereotype.Service;

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
        return managementServiceClient.getAllSensors();
    }

    public Optional<Sensor> findById(UUID sensorId) {
        return managementServiceClient.getSensorById(sensorId);
    }


    public Sensor updateMeasures(UUID sensorId, float newTemperature, float newHumidity) {
        return managementServiceClient.updateMeasures(sensorId, newTemperature, newHumidity);
    }
}
