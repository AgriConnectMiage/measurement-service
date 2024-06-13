package fr.miage.acm.measurementservice.client;

import fr.miage.acm.measurementservice.device.actuator.watering.scheduler.WateringScheduler;
import fr.miage.acm.measurementservice.device.sensor.Sensor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "management-service")
public interface ManagementServiceClient {

        @GetMapping("/actuators/{actuatorId}/watering-scheduler")
        Optional<WateringScheduler> findByActuator(@PathVariable("actuatorId") UUID actuatorId);

        // find all sensors
        @GetMapping("/sensors")
        List<Sensor> getAllSensors();

        // find sensor by id
        @GetMapping("/sensors/{sensorId}")
        Optional<Sensor> getSensorById(@PathVariable("sensorId") UUID sensorId);


        @PostMapping("/sensors/{sensorId}/measure")
        void updateSensorMeasure(@PathVariable("sensorId") UUID sensorId, Sensor sensor);


}
