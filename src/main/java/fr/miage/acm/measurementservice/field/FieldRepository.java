package fr.miage.acm.measurementservice.field;

import fr.miage.acm.measurementservice.device.sensor.Sensor;
import fr.miage.acm.measurementservice.farmer.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FieldRepository extends JpaRepository<Field, UUID> {
    List<Field> findByFarmer(Farmer farmer);

    void deleteByFarmer(Farmer farmer);

}
