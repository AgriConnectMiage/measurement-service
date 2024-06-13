package fr.miage.acm.measurementservice.device.sensor;

import fr.miage.acm.measurementservice.device.Device;
import fr.miage.acm.measurementservice.device.DeviceState;
import fr.miage.acm.measurementservice.farmer.Farmer;
import fr.miage.acm.measurementservice.field.Field;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Sensor extends Device {
    // Interval between two measurements in seconds
    private int interval;

    @OneToOne
    @JoinColumn(name = "field_id")
    private Field field;
    @Column(columnDefinition = "NUMERIC(5,1)")
    private Float lastTemperatureMeasured;
    @Column(columnDefinition = "NUMERIC(5,1)")
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

        public void setState(DeviceState newState) {
        if ((newState == DeviceState.OFF || newState == DeviceState.ON) && this.getField() == null) {
            throw new IllegalStateException("Cannot change state to " + newState + " of actuator without field");
        }
        if (newState == DeviceState.NOT_ASSIGNED && this.getField() != null) {
            throw new IllegalStateException("Cannot change state to " + newState + " of actuator assigned to a field");
        }
        this.state = newState;
        return;
    }

    public void setInterval(int interval) {
        if (interval <= 0) {
            throw new IllegalArgumentException("Interval must be positive");
        }
        this.interval = interval;
    }
}
