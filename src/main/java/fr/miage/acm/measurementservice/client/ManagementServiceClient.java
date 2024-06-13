package fr.miage.acm.measurementservice.client;

import fr.miage.acm.measurementservice.api.ApiActuator;
import fr.miage.acm.measurementservice.api.ApiWateringScheduler;
import fr.miage.acm.measurementservice.device.sensor.Sensor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "management-service")
public interface ManagementServiceClient {

    @GetMapping("/actuators/{actuatorId}/watering-scheduler")
    Optional<ApiWateringScheduler> findByActuator(@PathVariable("actuatorId") UUID actuatorId);

    @GetMapping("/sensors")
    List<Sensor> getAllSensors();

    @GetMapping("/sensors/{sensorId}")
    Optional<Sensor> getSensorById(@PathVariable("sensorId") UUID sensorId);

    // update temperature and humidity of sensor
    @PostMapping("/sensors/{sensorId}/measure")
    void updateSensorMeasure(@PathVariable("sensorId") UUID sensorId,
                             @RequestParam("temperature") float temperature,
                             @RequestParam("humidity") float humidity);

    // find actuator by field
    @GetMapping("/actuators/field/{fieldId}")
    Optional<ApiActuator> findActuatorByField(@PathVariable("fieldId") UUID fieldId);
}
