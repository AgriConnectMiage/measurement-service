package fr.miage.acm.measurementservice.device.sensor;

import fr.miage.acm.measurementservice.api.ApiSensor;
import fr.miage.acm.measurementservice.device.Device;
import fr.miage.acm.measurementservice.farmer.Farmer;
import fr.miage.acm.measurementservice.field.Field;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Sensor extends Device {
    // Interval between two measurements in seconds
    private int interval;

    private Field field;
    private Float lastTemperatureMeasured;
    private Float lastHumidityMeasured;

    private LocalDateTime lastMeasurementTime;

    public Sensor(Farmer farmer) {
        super(farmer);
        this.interval = 5;
        this.field = null;
        this.lastTemperatureMeasured = null;
        this.lastHumidityMeasured = null;
        this.lastMeasurementTime = null;
    }

    public Sensor() {
        // Default constructor required by JPA
    }


    public Sensor(ApiSensor apiSensor) {
        super(new Farmer(apiSensor.getFarmer()), apiSensor.getState(), apiSensor.getId());
        this.interval = apiSensor.getInterval();
        this.field = apiSensor.getField();
        this.lastTemperatureMeasured = apiSensor.getLastTemperatureMeasured();
        this.lastHumidityMeasured = apiSensor.getLastHumidityMeasured();
        this.lastMeasurementTime = apiSensor.getLastMeasurementTime();
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "interval=" + interval +
                ", state=" + getState() +
                ", farmer=" + getFarmer() +
                ", field=" + getField() +
                ", lastTemperatureMeasured=" + getLastTemperatureMeasured() +
                ", lastHumidityMeasured=" + getLastHumidityMeasured() +
                '}';
    }

    public void setInterval(int interval) {
        if (interval <= 0) {
            throw new IllegalArgumentException("Interval must be positive");
        }
        this.interval = interval;
    }
}
