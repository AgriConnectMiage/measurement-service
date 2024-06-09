package fr.miage.acm.measurementservice.device.sensor;

import fr.miage.acm.measurementservice.api.ApiSensor;
import fr.miage.acm.measurementservice.device.DeviceState;
import fr.miage.acm.measurementservice.farmer.Farmer;
import fr.miage.acm.measurementservice.field.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public List<ApiSensor> getAllSensors() {
        return sensorService.findAll().stream()
                .map(ApiSensor::new)
                .toList();
    }

    @GetMapping("/{id}")
    public Optional<Sensor> getSensorById(@PathVariable UUID id) {
        return sensorService.findById(id);
    }

    @GetMapping("/farmer/{farmerId}")
    public List<Sensor> getSensorsByFarmer(@PathVariable UUID farmerId) {
        Farmer farmer = new Farmer();
        farmer.setId(farmerId);
        return sensorService.findByFarmer(farmer);
    }

    @PostMapping
    public Sensor createSensor(@RequestBody Farmer farmer) {
        return sensorService.addSensor(farmer);
    }

    @DeleteMapping("/{id}")
    public void deleteSensor(@PathVariable UUID id) {
        Optional<Sensor> sensor = sensorService.findById(id);
        sensor.ifPresent(sensorService::delete);
    }

    @PostMapping("/{id}/assign")
    public Sensor assignSensorToField(@PathVariable UUID id, @RequestBody Field field) {
        Optional<Sensor> sensor = sensorService.findById(id);
        if (sensor.isPresent()) {
            return sensorService.assignSensorToField(sensor.get(), field);
        }
        return null;
    }

    @PostMapping("/{id}/unassign")
    public Sensor unassignSensorFromField(@PathVariable UUID id) {
        Optional<Sensor> sensor = sensorService.findById(id);
        if (sensor.isPresent()) {
            return sensorService.unassignSensorFromField(sensor.get());
        }
        return null;
    }

    @PostMapping("/{id}/state")
    public Sensor changeSensorState(@PathVariable UUID id, @RequestParam DeviceState state) {
        Optional<Sensor> sensor = sensorService.findById(id);
        if (sensor.isPresent()) {
            return sensorService.changeState(sensor.get(), state);
        }
        return null;
    }

    @PostMapping("/{id}/interval")
    public Sensor changeSensorInterval(@PathVariable UUID id, @RequestParam int interval) {
        Optional<Sensor> sensor = sensorService.findById(id);
        if (sensor.isPresent()) {
            return sensorService.changeInterval(sensor.get(), interval);
        }
        return null;
    }

    @PostMapping("/interval")
    public void changeSensorsInterval(@RequestBody List<UUID> sensorIds, @RequestParam int interval) {
        List<Sensor> sensors = sensorService.findAllByIds(sensorIds);
        sensorService.changeInterval(sensors, interval);
    }
}
