package fr.miage.acm.measurementservice.device.measurement;

import fr.miage.acm.measurementservice.api.ApiWateringScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/sensor/{sensorId}/unschedule")
    public void unscheduleSensorTask(@PathVariable Long sensorId) {
        measurementService.unscheduleSensorTask(sensorId);
    }
}
