package fr.miage.acm.measurementservice.field;

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

    private WateringSchedulerService wateringSchedulerService;


    public FieldService(WateringSchedulerService wateringSchedulerService) {
        this.wateringSchedulerService = wateringSchedulerService;
    }

    public boolean isFieldGettingWatered(Field field) {
        Optional<Actuator> optionalActuator = actuatorRepository.findByField(field);
        if (optionalActuator.isEmpty()) {
            return false;
        }
        Actuator actuator = optionalActuator.get();

        // get watering scheduler for actuator
        Optional<WateringScheduler> optionalWateringScheduler = wateringSchedulerService.findByActuator(actuator);
        if (optionalWateringScheduler.isEmpty()) {
            return false;
        }
        WateringScheduler wateringScheduler = optionalWateringScheduler.get();
        return wateringSchedulerService.isWateringInProgress(wateringScheduler);

    }

    // check if the field has an actuator which has an actuator with intelligent watering (humidity threshold != null)
    public void checkForIntelligentWatering(Field field, float newHumidity) {
        Optional<Actuator> optionalActuator = actuatorRepository.findByField(field);
        if (optionalActuator.isEmpty()) {
            return;
        }
        Actuator actuator = optionalActuator.get();

        // get watering scheduler for actuator
        Optional<WateringScheduler> optionalWateringScheduler = wateringSchedulerService.findByActuator(actuator);
        if (optionalWateringScheduler.isEmpty()) {
            return;
        }
        WateringScheduler wateringScheduler = optionalWateringScheduler.get();
        // if humidity threshold is set and humidity is below threshold and both beginDate and endDate are null, trigger intelligent watering
        if (wateringScheduler.getHumidityThreshold() != null && newHumidity < wateringScheduler.getHumidityThreshold()
                && wateringScheduler.getBeginDate() == null && wateringScheduler.getEndDate() == null) {
            wateringSchedulerService.triggerIntelligentWatering(actuator, wateringScheduler);
        }
    }
}
