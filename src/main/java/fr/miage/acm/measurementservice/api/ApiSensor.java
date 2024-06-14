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
public class ApiSensor extends ApiDevice {

    private UUID id;
    private DeviceState state;
    private int interval;
    private Field field;
    private Float lastTemperatureMeasured;
    private Float lastHumidityMeasured;
    private LocalDateTime lastMeasurementTime;

    public ApiSensor(Sensor sensor) {
        super(new ApiFarmer(sensor.getFarmer()), sensor.getId(), sensor.getState());
        this.id = sensor.getId();
        this.state = sensor.getState();
        this.interval = sensor.getInterval();
        this.field = sensor.getField();
        this.lastTemperatureMeasured = sensor.getLastTemperatureMeasured();
        this.lastHumidityMeasured = sensor.getLastHumidityMeasured();
        this.lastMeasurementTime = sensor.getLastMeasurementTime();
    }

    public ApiSensor() {
    }

    public ApiSensor(UUID id, DeviceState state, int interval, Field field, Float lastTemperatureMeasured, Float lastHumidityMeasured, LocalDateTime lastMeasurementTime, ApiFarmer apiFarmer) {
        super(apiFarmer, id, state);
        this.id = id;
        this.state = state;
        this.interval = interval;
        this.field = field;
        this.lastTemperatureMeasured = lastTemperatureMeasured;
        this.lastHumidityMeasured = lastHumidityMeasured;
        this.lastMeasurementTime = lastMeasurementTime;
    }

    // toString method
    @Override
    public String toString() {
        return "ApiSensor{" +
                "id=" + id +
                ", state=" + state +
                ", interval=" + interval +
                ", field=" + field +
                ", farmer=" + getFarmer() +
                ", lastTemperatureMeasured=" + lastTemperatureMeasured +
                ", lastHumidityMeasured=" + lastHumidityMeasured +
                ", lastMeasurementTime=" + lastMeasurementTime +
                '}';
    }
}
