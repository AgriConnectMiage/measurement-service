package fr.miage.acm.measurementservice.api;

import fr.miage.acm.measurementservice.device.DeviceState;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class ApiDevice {

    private UUID id;

    private DeviceState state;

    private ApiFarmer farmer;

    public ApiDevice(ApiFarmer farmer, UUID id, DeviceState state) {
        this.state = state;
        this.farmer = farmer;
        this.id = id;
    }

    public ApiDevice() {
        // Default constructor required by JPA
    }
}
