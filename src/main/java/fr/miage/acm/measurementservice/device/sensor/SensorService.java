package fr.miage.acm.measurementservice.device.sensor;

import fr.miage.acm.measurementservice.api.ApiSensor;
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
        // log before return
        List<Sensor> sensors = managementServiceClient.getAllSensors();
        System.out.println("Sensors: " + sensors);
        return sensors;
//        return managementServiceClient.getAllSensors();
    }

    public Sensor findById(UUID sensorId) {
        Optional<ApiSensor> optionalApiSensor = managementServiceClient.getSensorById(sensorId);
        if(optionalApiSensor.isPresent()) {
            ApiSensor apiSensor = optionalApiSensor.get();
            System.out.println(apiSensor);
            System.out.println(new Sensor(apiSensor));
            return new Sensor(apiSensor);
        }
        return null;
    }


    public void updateMeasures(UUID sensorId, float newTemperature, float newHumidity) {
        managementServiceClient.updateSensorMeasure(sensorId, newTemperature, newHumidity);
    }
}
