package fr.miage.acm.measurementservice.device.measurement;

import fr.miage.acm.measurementservice.api.ApiWateringScheduler;
import fr.miage.acm.measurementservice.device.actuator.WateringScheduler;
import fr.miage.acm.measurementservice.device.sensor.Sensor;
import fr.miage.acm.measurementservice.device.sensor.SensorService;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class MeasurementService {

    private MeasurementRepository measurementRepository;
    private final Random random = new Random();
    private final TaskScheduler taskScheduler;
    private final SensorService sensorService;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public MeasurementService(MeasurementRepository measurementRepository, TaskScheduler taskScheduler, SensorService sensorService) {
        this.measurementRepository = measurementRepository;
        this.taskScheduler = taskScheduler;
        this.sensorService = sensorService;
    }

    public Measurement createWateringMeasurement(ApiWateringScheduler apiWateringScheduler) {
        Measurement measurement = new Measurement();
        measurement.setDateTime(LocalDateTime.now());
        measurement.setFarmerEmail(apiWateringScheduler.getActuator().getFarmer().getEmail());
        measurement.setFieldCoord(apiWateringScheduler.getActuator().getField().getCoord());
        measurement.setDuration(apiWateringScheduler.getDuration());
        measurement.setDeviceId(apiWateringScheduler.getActuator().getId());
        measurementRepository.save(measurement);
        return measurement;
    }

    @PostConstruct
    public void scheduleSensorTasks() {
        List<Sensor> sensors = sensorService.findAll();
        for (Sensor sensor : sensors) {
            scheduleSensorTask(sensor);
        }
    }

    private void scheduleSensorTask(Sensor sensor) {
        // Scheduled task to generate a temperature measurement every interval
        Long sensorIdAsLong = sensor.getId().getLeastSignificantBits();
        long intervalInMillis = (long) (sensor.getInterval() * 1000);
        ScheduledFuture<?> scheduledTask = taskScheduler.scheduleAtFixedRate(() -> generateTemperatureMeasurement(sensor), intervalInMillis);
        scheduledTasks.put(sensorIdAsLong, scheduledTask);
    }


    private void generateTemperatureMeasurement(Sensor sensor) {
        Measurement measurement = new Measurement();
        measurement.setDateTime(LocalDateTime.now());
        measurement.setTemperature(generateRandomTemperature());
        measurement.setDeviceId(sensor.getId());
        // Définissez les autres champs nécessaires pour l'enregistrement de la mesure
        // measurement.setFarmerEmail(...);
        // measurement.setFieldCoord(...);

        measurementRepository.save(measurement);
    }

    private Float generateRandomTemperature() {
        // Random generation of a temperature between 0 and 45 degrees
        return random.nextFloat() * 45;
    }

    public void unscheduleSensorTask(Long sensorId) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(sensorId);
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            scheduledTasks.remove(sensorId);
        }
    }

}
