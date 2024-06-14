package fr.miage.acm.measurementservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(name = "watering-service")
public interface WateringServiceClient {

    @PostMapping("/actuators/{actuatorId}/watering-scheduler/{schedulerId}/intelligent-watering")
    void triggerIntelligentWatering(@PathVariable("actuatorId") UUID actuatorId,
                                    @PathVariable("schedulerId") UUID schedulerId);

}
