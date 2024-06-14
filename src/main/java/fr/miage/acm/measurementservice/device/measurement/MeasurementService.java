package fr.miage.acm.measurementservice.device.measurement;

import fr.miage.acm.measurementservice.api.ApiWateringScheduler;
import fr.miage.acm.measurementservice.device.DeviceState;
import fr.miage.acm.measurementservice.device.sensor.Sensor;
import fr.miage.acm.measurementservice.device.sensor.SensorService;
import fr.miage.acm.measurementservice.field.Field;
import fr.miage.acm.measurementservice.field.FieldService;
import jakarta.annotation.PostConstruct;
import org.springframework.cloud.logging.LoggingRebinder;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final Random random = new Random();
    private final TaskScheduler taskScheduler;
    private final SensorService sensorService;
    private final Map<UUID, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final FieldService fieldService;
    private final LoggingRebinder loggingRebinder;

    public MeasurementService(MeasurementRepository measurementRepository, TaskScheduler taskScheduler,
                              SensorService sensorService, FieldService fieldService, LoggingRebinder loggingRebinder) {
        this.measurementRepository = measurementRepository;
        this.taskScheduler = taskScheduler;
        this.sensorService = sensorService;
        this.fieldService = fieldService;
        this.loggingRebinder = loggingRebinder;
    }

    public Measurement createWateringMeasurement(ApiWateringScheduler apiWateringScheduler) {
        Measurement measurement = new Measurement();
        measurement.setDateTime(LocalDateTime.now());
        measurement.setFarmerId(apiWateringScheduler.getActuator().getFarmer().getId());
        measurement.setFieldCoord(apiWateringScheduler.getActuator().getField().getCoord());
        measurement.setWateringDuration(apiWateringScheduler.getDuration());
        measurement.setDeviceId(apiWateringScheduler.getActuator().getId());
        measurementRepository.save(measurement);
        System.out.println("New watering measurement: " + measurement);
        return measurement;
    }

    @PostConstruct
    public void scheduleSensorTasks() {
        List<Sensor> sensors = sensorService.findAll();
        for (Sensor sensor : sensors) {
            if (sensor.getState() == DeviceState.ON) {
                scheduleSensorTask(sensor);
            }
        }
    }

    private void scheduleSensorTask(Sensor sensor) {
        // Scheduled task to generate a temperature measurement every interval
        long intervalInMillis = (long) (sensor.getInterval() * 1000);
        ScheduledFuture<?> scheduledTask = taskScheduler.scheduleAtFixedRate(() -> {
            generateSensorMeasurement(sensor);
        }, intervalInMillis);
        scheduledTasks.put(sensor.getId(), scheduledTask);
    }

    public void scheduleSensorTask(UUID sensorId) {
        Sensor sensor = sensorService.findById(sensorId);
        if (sensor != null) {
            scheduleSensorTask(sensor);
        }
    }

    private void generateSensorMeasurement(Sensor sensor) {
        Measurement measurement = new Measurement();
        measurement.setDateTime(LocalDateTime.now());
        measurement.setDeviceId(sensor.getId());
        measurement.setFarmerId(sensor.getFarmer().getId());
        measurement.setFieldCoord(sensor.getField().getCoord());

        // log sensor last humidity and temperature
        float newTemperature = generateRandomTemperature(sensor);
        measurement.setTemperature(newTemperature);
        float newHumidity = generateRandomHumidity(sensor);
        measurement.setHumidity(newHumidity);

        // if field has intelligent watering, check if watering is needed and trigger it
        fieldService.checkForIntelligentWatering(sensor.getField(), newHumidity);
        System.out.println("New sensor measurement: " + measurement);
        measurementRepository.save(measurement);
        sensorService.updateMeasures(sensor.getId(), newTemperature, newHumidity);
    }

    private Float generateRandomTemperature(Sensor sensor) {
        float newTemperature;
        if (sensor.getLastTemperatureMeasured() == null) {
            newTemperature = (float) (Math.round((random.nextFloat() * 45) * 10) / 10.0);
        } else if (fieldService.isFieldGettingWatered(sensor.getField())) {
            LocalDateTime lastMeasurementTime = sensor.getLastMeasurementTime();
            LocalDateTime now = LocalDateTime.now();
            long secondsElapsed = ChronoUnit.SECONDS.between(lastMeasurementTime, now);

            float decreaseRate = 0.1f;
            float decreaseAmount = decreaseRate * secondsElapsed;
            newTemperature = sensor.getLastTemperatureMeasured() - decreaseAmount;

            if (newTemperature < 0) {
                newTemperature = 0;
            }
            newTemperature = Math.round(newTemperature * 10) / 10.0f;
        } else {
            float variation = (random.nextFloat() - 0.5f) * 1.0f;
            newTemperature = sensor.getLastTemperatureMeasured() + variation;

            if (newTemperature < 0) {
                newTemperature = 0;
            } else if (newTemperature > 45) {
                newTemperature = 45;
            }
            newTemperature = Math.round(newTemperature * 10) / 10.0f;
        }
        return newTemperature;
    }

    private Float generateRandomHumidity(Sensor sensor) {
        float newHumidity;
        Field field = sensor.getField();

        if (sensor.getLastHumidityMeasured() == null) {
            newHumidity = (float) (Math.round((random.nextFloat() * 100) * 10) / 10.0);
        } else if (fieldService.isFieldGettingWatered(field)) {
            LocalDateTime lastMeasurementTime = sensor.getLastMeasurementTime();
            LocalDateTime now = LocalDateTime.now();
            long secondsElapsed = ChronoUnit.SECONDS.between(lastMeasurementTime, now);

            float increaseRate = 0.1f;
            float increaseAmount = increaseRate * secondsElapsed;
            newHumidity = sensor.getLastHumidityMeasured() + increaseAmount;

            if (newHumidity > 100) {
                newHumidity = 100;
            }
            newHumidity = Math.round(newHumidity * 10) / 10.0f;
        } else {
            float variation = (random.nextFloat() - 0.5f) * 1.0f;
            newHumidity = sensor.getLastHumidityMeasured() + variation;

            if (newHumidity < 0) {
                newHumidity = 0;
            } else if (newHumidity > 100) {
                newHumidity = 100;
            }
            newHumidity = Math.round(newHumidity * 10) / 10.0f;
        }
        return newHumidity;
    }

    public void unscheduleSensorTask(UUID sensorId) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(sensorId);
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            scheduledTasks.remove(sensorId);
        }
    }

    public void changeSensorInterval(UUID sensorId, int interval) {
        synchronized (this) {
            unscheduleSensorTask(sensorId);
            Sensor sensor = sensorService.findById(sensorId);
            if (sensor != null) {
                sensor.setInterval(interval);
                scheduleSensorTask(sensor);
            }
        }
    }
}
