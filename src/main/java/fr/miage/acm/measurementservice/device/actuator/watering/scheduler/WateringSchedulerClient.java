package fr.miage.acm.measurementservice.device.actuator.watering.scheduler;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "watering-service")
public interface WateringSchedulerClient {

    @PostMapping("/actuators/{actuatorId}/watering-scheduler/{schedulerId}/intelligent-watering")
    void triggerIntelligentWatering(@PathVariable("actuatorId") UUID actuatorId,
                                    @PathVariable("schedulerId") UUID schedulerId);
}
