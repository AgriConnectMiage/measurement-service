package fr.miage.acm.measurementservice.api;

import fr.miage.acm.measurementservice.device.DeviceState;
import fr.miage.acm.measurementservice.device.actuator.Actuator;
import fr.miage.acm.measurementservice.field.Field;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ApiActuator {

    private UUID id;
    private DeviceState state;
    private Field field;

    public ApiActuator(Actuator actuator) {
        this.id = actuator.getId();
        this.state = actuator.getState();
        this.field = actuator.getField();
    }
}
