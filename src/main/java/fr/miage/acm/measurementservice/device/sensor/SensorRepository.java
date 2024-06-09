package fr.miage.acm.measurementservice.device.sensor;

import fr.miage.acm.measurementservice.farmer.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SensorRepository extends JpaRepository<Sensor, UUID> {
    void deleteByFarmer(Farmer farmer);

    List<Sensor> findByFarmer(Farmer farmer);

    // findAllByIds
    List<Sensor> findAllByIdIn(List<UUID> ids);
}
