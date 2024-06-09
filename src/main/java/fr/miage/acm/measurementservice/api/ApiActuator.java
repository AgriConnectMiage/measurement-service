package fr.miage.acm.measurementservice.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.miage.acm.measurementservice.device.DeviceState;
import fr.miage.acm.measurementservice.device.actuator.Actuator;
import fr.miage.acm.measurementservice.farmer.Farmer;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ApiActuator extends ApiDevice {

    private UUID id;
    private DeviceState state;
    private ApiField field;

    public ApiActuator(Actuator actuator) {
        super(new ApiFarmer(actuator.getFarmer()));
        this.id = actuator.getId();
        this.state = actuator.getState();
        this.field = new ApiField(actuator.getField());
    }

    public ApiActuator() {
    }

    public ApiActuator(UUID id, DeviceState state, ApiField field, ApiFarmer apiFarmer) {
        super(apiFarmer);
        this.id = id;
        this.state = state;
        this.field = field;
    }
}
