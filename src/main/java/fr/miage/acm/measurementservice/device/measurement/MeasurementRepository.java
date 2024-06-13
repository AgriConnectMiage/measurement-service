package fr.miage.acm.measurementservice.device.measurement;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.UUID;

public interface MeasurementRepository extends Neo4jRepository<Measurement, UUID> {
}
