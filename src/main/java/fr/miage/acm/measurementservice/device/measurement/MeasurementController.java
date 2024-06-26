package fr.miage.acm.measurementservice.device.measurement;

import fr.miage.acm.measurementservice.api.ApiWateringScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;

    @Autowired
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @PostMapping("/watering")
    public Measurement createWateringMeasurement(@RequestBody ApiWateringScheduler apiWateringScheduler) {
        return measurementService.createWateringMeasurement(apiWateringScheduler);
    }

    @PostMapping("/sensor/{sensorId}/schedule")
    public void scheduleSensorTask(@PathVariable UUID sensorId) {
        System.out.println("Scheduling sensor task for sensor " + sensorId);
        measurementService.scheduleSensorTask(sensorId);
    }

    @DeleteMapping("/sensor/{sensorId}/schedule")
    public void unscheduleSensorTask(@PathVariable UUID sensorId) {
        System.out.println("Unscheduling sensor task for sensor " + sensorId);
        measurementService.unscheduleSensorTask(sensorId);
    }

    @PutMapping("/sensor/{sensorId}/interval")
    public void changeSensorInterval(@PathVariable UUID sensorId, @RequestParam int interval) {
        System.out.println("Changing interval of sensor " + sensorId + " to " + interval);
        measurementService.changeSensorInterval(sensorId, interval);
    }

}
