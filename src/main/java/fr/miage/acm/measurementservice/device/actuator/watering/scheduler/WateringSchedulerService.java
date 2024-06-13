package fr.miage.acm.measurementservice.device.actuator.watering.scheduler;

import fr.miage.acm.measurementservice.client.ManagementServiceClient;
import fr.miage.acm.measurementservice.client.WateringServiceClient;
import fr.miage.acm.measurementservice.device.actuator.Actuator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WateringSchedulerService {

    private final WateringServiceClient wateringServiceClient;
    private  final ManagementServiceClient managementServiceClient;


    public WateringSchedulerService(WateringServiceClient wateringServiceClient, ManagementServiceClient managementServiceClient) {
        this.wateringServiceClient = wateringServiceClient;
        this.managementServiceClient = managementServiceClient;
    }

    public boolean isWateringInProgress(WateringScheduler wateringScheduler) {
        LocalDateTime now = LocalDateTime.now();
        if (wateringScheduler.getBeginDate() == null || wateringScheduler.getEndDate() == null) {
            return false;
        }
        return wateringScheduler.getBeginDate().isBefore(now) && wateringScheduler.getEndDate().isAfter(now);
    }


    public Optional<WateringScheduler> findByActuator(Actuator actuator) {
        return managementServiceClient.findByActuator(actuator.getId());
    }

    public void triggerIntelligentWatering(Actuator actuator, WateringScheduler wateringScheduler) {
        System.out.println("Triggering intelligent watering for actuator " + actuator.getId() + " and watering scheduler " + wateringScheduler.getId());
        wateringServiceClient.triggerIntelligentWatering(actuator.getId(), wateringScheduler.getId());
    }
}
