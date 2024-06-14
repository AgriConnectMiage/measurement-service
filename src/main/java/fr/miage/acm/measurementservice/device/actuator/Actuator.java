package fr.miage.acm.measurementservice.device.actuator;

import fr.miage.acm.measurementservice.api.ApiActuator;
import fr.miage.acm.measurementservice.device.Device;
import fr.miage.acm.measurementservice.device.DeviceState;
import fr.miage.acm.measurementservice.farmer.Farmer;
import fr.miage.acm.measurementservice.field.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Actuator extends Device {

    private Field field;

    public Actuator(Farmer farmer) {
        super(farmer);
        this.field = null;
    }

    public Actuator() {
        // Default constructor required by JPA
    }

    public Actuator(ApiActuator apiActuator) {
        super(new Farmer(apiActuator.getFarmer()), apiActuator.getState(), apiActuator.getId());
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
