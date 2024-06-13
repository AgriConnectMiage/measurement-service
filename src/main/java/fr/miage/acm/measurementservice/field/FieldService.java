package fr.miage.acm.measurementservice.field;

import fr.miage.acm.measurementservice.api.ApiActuator;
import fr.miage.acm.measurementservice.api.ApiWateringScheduler;
import fr.miage.acm.measurementservice.client.ManagementServiceClient;
import fr.miage.acm.measurementservice.device.actuator.Actuator;
import fr.miage.acm.measurementservice.device.actuator.watering.scheduler.WateringScheduler;
import fr.miage.acm.measurementservice.device.actuator.watering.scheduler.WateringSchedulerService;
import fr.miage.acm.measurementservice.farmer.Farmer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FieldService {

    private final ManagementServiceClient managementServiceClient;
    private WateringSchedulerService wateringSchedulerService;


    public FieldService(WateringSchedulerService wateringSchedulerService, ManagementServiceClient managementServiceClient) {
        this.wateringSchedulerService = wateringSchedulerService;
        this.managementServiceClient = managementServiceClient;
    }

    public boolean isFieldGettingWatered(Field field) {
        Optional<ApiActuator> optionalApiActuator = managementServiceClient.findActuatorByField(field.getId());
        if (optionalApiActuator.isEmpty()) {
            return false;
        }
        Actuator actuator = new Actuator(optionalApiActuator.get());

        // get watering scheduler for actuator
        Optional<ApiWateringScheduler> optionalApiWateringScheduler = wateringSchedulerService.findByActuator(actuator);
        if (optionalApiWateringScheduler.isEmpty()) {
            return false;
        }
        WateringScheduler wateringScheduler = new WateringScheduler(optionalApiWateringScheduler.get());
        return wateringSchedulerService.isWateringInProgress(wateringScheduler);

    }

    // check if the field has an actuator which has an actuator with intelligent watering (humidity threshold != null)
    public void checkForIntelligentWatering(Field field, float newHumidity) {
        Optional<ApiActuator> optionalApiActuator = managementServiceClient.findActuatorByField(field.getId());
        if (optionalApiActuator.isEmpty()) {
            return;
        }
        Actuator actuator = new Actuator(optionalApiActuator.get());

        // get watering scheduler for actuator
        Optional<ApiWateringScheduler> optionalWateringScheduler = wateringSchedulerService.findByActuator(actuator);
        if (optionalWateringScheduler.isEmpty()) {
            return;
        }
        WateringScheduler wateringScheduler = new WateringScheduler(optionalWateringScheduler.get());
        // if humidity threshold is set and humidity is below threshold and both beginDate and endDate are null, trigger intelligent watering
        if (wateringScheduler.getHumidityThreshold() != null && newHumidity < wateringScheduler.getHumidityThreshold()
                && wateringScheduler.getBeginDate() == null && wateringScheduler.getEndDate() == null) {
            wateringSchedulerService.triggerIntelligentWatering(actuator, wateringScheduler);
        }
    }
}
