package fr.miage.acm.measurementservice.device.measurement;

import fr.miage.acm.measurementservice.api.ApiWateringScheduler;
import fr.miage.acm.measurementservice.device.DeviceState;
import fr.miage.acm.measurementservice.device.sensor.Sensor;
import fr.miage.acm.measurementservice.device.sensor.SensorService;
import fr.miage.acm.measurementservice.field.Field;
import fr.miage.acm.measurementservice.field.FieldService;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class MeasurementService {

    private MeasurementRepository measurementRepository;
    private final Random random = new Random();
    private final TaskScheduler taskScheduler;
    private final SensorService sensorService;
    private final Map<UUID, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final FieldService fieldService;

    public MeasurementService(MeasurementRepository measurementRepository, TaskScheduler taskScheduler, SensorService sensorService, FieldService fieldService) {
        this.measurementRepository = measurementRepository;
        this.taskScheduler = taskScheduler;
        this.sensorService = sensorService;
        this.fieldService = fieldService;
    }

    public Measurement createWateringMeasurement(ApiWateringScheduler apiWateringScheduler) {
        Measurement measurement = new Measurement();
        measurement.setDateTime(LocalDateTime.now());
        measurement.setFarmerId(apiWateringScheduler.getActuator().getFarmer().getId());
        measurement.setFieldCoord(apiWateringScheduler.getActuator().getField().getCoord());
        measurement.setWateringDuration(apiWateringScheduler.getDuration());
        measurement.setDeviceId(apiWateringScheduler.getActuator().getId());
        measurementRepository.save(measurement);
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


    // Schedule sensor task with id
    public void scheduleSensorTask(UUID sensorId) {
        Optional<Sensor> sensor = sensorService.findById(sensorId);
        sensor.ifPresent(this::scheduleSensorTask);
    }


    private void generateSensorMeasurement(Sensor sensor) {
        Measurement measurement = new Measurement();
        measurement.setDateTime(LocalDateTime.now());
        measurement.setDeviceId(sensor.getId());
        measurement.setFarmerId(sensor.getFarmer().getId());
        measurement.setFieldCoord(sensor.getField().getCoord());

        float newTemperature = generateRandomTemperature(sensor);
        measurement.setTemperature(newTemperature);
        float newHumiditiy = generateRandomHumidity(sensor);
        measurement.setHumidity(newHumiditiy);

        sensor.setLastTemperatureMeasured(newTemperature);
        sensor.setLastHumidityMeasured(newHumiditiy);
        sensor.setLastMeasurementTime(LocalDateTime.now());
        System.out.println("New measurement: " + measurement);
        measurementRepository.save(measurement);
        sensorService.save(sensor);
    }

    private Float generateRandomTemperature(Sensor sensor) {
        // Generate a random variation between -0.5 and +0.5 degrees
        float newTemperature;
        // Just generate a random temperature between 0 and 45 degrees if current temperature is null
        if (sensor.getLastTemperatureMeasured() == null) {
            newTemperature = (float) (Math.round((random.nextFloat() * 45) * 10) / 10.0);
        }
        // if field is getting watered, the temperature decreases based on the time elapsed
        else if (fieldService.isFieldGettingWatered(sensor.getField())) {
            LocalDateTime lastMeasurementTime = sensor.getLastMeasurementTime();
            LocalDateTime now = LocalDateTime.now();
            long secondsElapsed = ChronoUnit.SECONDS.between(lastMeasurementTime, now);

            // Decrease temperature based on time elapsed, e.g., 0.1 degree per second
            float decreaseRate = 0.1f;
            float decreaseAmount = decreaseRate * secondsElapsed;
            newTemperature = sensor.getLastTemperatureMeasured() - decreaseAmount;

            if (newTemperature < 0) {
                newTemperature = 0;
            }
            newTemperature = Math.round(newTemperature * 10) / 10.0f;
        }
        // Otherwise, the new temperature is a random variation between -0.5 and +0.5 degrees
        else {
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
        // Generate a random variation between -0.5 and +0.5%
        float newHumidity;
        // get field linked to sensor
        Field field = sensor.getField();

        // Just generate a random humidity between 0 and 100% if current humidity is null
        if (sensor.getLastHumidityMeasured() == null) {
            newHumidity = (float) (Math.round((random.nextFloat() * 100) * 10) / 10.0);
        }
        // if field is getting watered, the humidity increases based on the time elapsed
        // e.g., 0.1% per second
        else if (fieldService.isFieldGettingWatered(field)) {
            LocalDateTime lastMeasurementTime = sensor.getLastMeasurementTime();
            LocalDateTime now = LocalDateTime.now();
            long secondsElapsed = ChronoUnit.SECONDS.between(lastMeasurementTime, now);

            // Increase humidity based on time elapsed, e.g., 0.1% per second
            float increaseRate = 0.1f;
            float increaseAmount = increaseRate * secondsElapsed;
            newHumidity = sensor.getLastHumidityMeasured() + increaseAmount;

            if (newHumidity > 100) {
                newHumidity = 100;
            }
            newHumidity = Math.round(newHumidity * 10) / 10.0f;
        }
        // Otherwise, the new humidity is a random variation between -0.5 and +0.5%
        else {
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
        unscheduleSensorTask(sensorId);
        scheduleSensorTask(sensorId);
    }

}
