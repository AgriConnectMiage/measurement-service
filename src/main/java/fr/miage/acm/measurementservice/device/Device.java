package fr.miage.acm.measurementservice.device;

import fr.miage.acm.measurementservice.farmer.Farmer;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class Device {

    private UUID id;

    protected DeviceState state;

    private Farmer farmer;


    public Device(Farmer farmer) {
        this.state = DeviceState.NOT_ASSIGNED;
        this.farmer = farmer;
    }

    public Device(Farmer farmer, DeviceState state, UUID id) {
        this.state = state;
        this.farmer = farmer;
        this.id = id;
    }

    public Device() {
        // Default constructor required by JPA
    }

}
