package fr.miage.acm.measurementservice.device.actuator.watering.scheduler;

import fr.miage.acm.measurementservice.device.actuator.Actuator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WateringSchedulerRepository extends JpaRepository<WateringScheduler, UUID> {
    Optional<WateringScheduler> findByActuator(Actuator actuator);
}
