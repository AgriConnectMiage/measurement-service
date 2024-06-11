package fr.miage.acm.measurementservice.device.actuator.watering.scheduler;

import fr.miage.acm.measurementservice.device.DeviceState;
import fr.miage.acm.measurementservice.device.actuator.Actuator;
import fr.miage.acm.measurementservice.device.actuator.ActuatorService;
import fr.miage.acm.measurementservice.device.measurement.MeasurementService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class WateringSchedulerService {

    private final WateringSchedulerRepository wateringSchedulerRepository;


    public WateringSchedulerService(WateringSchedulerRepository wateringSchedulerRepository) {
        this.wateringSchedulerRepository = wateringSchedulerRepository;
    }

    public boolean isWateringInProgress(WateringScheduler wateringScheduler) {
        LocalDateTime now = LocalDateTime.now();
        return wateringScheduler.getBeginDate().isBefore(now) && wateringScheduler.getEndDate().isAfter(now);
    }

    public Optional<WateringScheduler> findById(UUID id) {
        return wateringSchedulerRepository.findById(id);
    }

    public Optional<WateringScheduler> findByActuator(Actuator actuator) {
        return wateringSchedulerRepository.findByActuator(actuator);
    }
}
