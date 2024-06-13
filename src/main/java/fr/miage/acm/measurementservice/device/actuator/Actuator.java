package fr.miage.acm.measurementservice.device.actuator;

import fr.miage.acm.measurementservice.api.ApiActuator;
import fr.miage.acm.measurementservice.device.Device;
import fr.miage.acm.measurementservice.device.DeviceState;
import fr.miage.acm.measurementservice.farmer.Farmer;
import fr.miage.acm.measurementservice.field.Field;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Actuator extends Device {

    @OneToOne
    @JoinColumn(name = "field_id")
    private Field field;

    public Actuator(Farmer farmer) {
        super(farmer);
        this.field = null;
    }

    public Actuator() {
        // Default constructor required by JPA
    }

    public Actuator(ApiActuator apiActuator) {
        super(new Farmer(apiActuator.getFarmer()));
        this.field = new Field(apiActuator.getField());
    }

    @Override
    public String toString() {
        return "Actuator{" +
                "id=" + getId() +
                ", state=" + getState() +
                ", field=" + getField() +
                ", farmer=" + getFarmer() +
                '}';
    }
}
