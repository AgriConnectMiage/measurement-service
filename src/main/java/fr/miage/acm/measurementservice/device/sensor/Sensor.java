package fr.miage.acm.measurementservice.device.sensor;

import fr.miage.acm.measurementservice.device.Device;
import fr.miage.acm.measurementservice.device.DeviceState;
import fr.miage.acm.measurementservice.farmer.Farmer;
import fr.miage.acm.measurementservice.field.Field;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Sensor extends Device {
    // Interval between two measurements in seconds
    private float interval;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;
    private Integer currentTemperature;
    private Integer currentHumidity;

    public Sensor(Farmer farmer) {
        super(farmer);
        this.interval = 5;
        this.field = null;
        this.currentTemperature = null;
        this.currentHumidity = null;
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
                ", currentTemperature=" + getCurrentTemperature() +
                ", currentHumidity=" + getCurrentHumidity() +
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

    public void setInterval(float interval) {
        if (interval <= 0) {
            throw new IllegalArgumentException("Interval must be positive");
        }
        this.interval = interval;
    }
}
