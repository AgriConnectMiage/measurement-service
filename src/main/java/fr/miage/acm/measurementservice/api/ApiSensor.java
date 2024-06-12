package fr.miage.acm.measurementservice.api;

import fr.miage.acm.measurementservice.device.DeviceState;
import fr.miage.acm.measurementservice.device.sensor.Sensor;
import fr.miage.acm.measurementservice.field.Field;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ApiSensor {

    private UUID id;
    private DeviceState state;
    private int interval;
    private Field field;
    private Float lastTemperatureMeasured;
    private Float lastHumidityMeasured;
    private LocalDateTime lastMeasurementTime;

    public ApiSensor(Sensor sensor) {
        this.id = sensor.getId();
        this.state = sensor.getState();
        this.interval = sensor.getInterval();
        this.field = sensor.getField();
        this.lastTemperatureMeasured = sensor.getLastTemperatureMeasured();
        this.lastHumidityMeasured = sensor.getLastHumidityMeasured();
        this.lastMeasurementTime = sensor.getLastMeasurementTime();
    }
}
