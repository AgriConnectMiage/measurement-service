package fr.miage.acm.measurementservice.device.measurement;


import fr.miage.acm.measurementservice.api.ApiWateringScheduler;
import fr.miage.acm.measurementservice.device.actuator.WateringScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MeasurementService {

    private MeasurementRepository measurementRepository;

    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public Measurement createWateringMeasurement(ApiWateringScheduler apiWateringScheduler) {
        Measurement measurement = new Measurement();
        measurement.setDateTime(LocalDateTime.now());
        measurement.setFarmerEmail(apiWateringScheduler.getActuator().getFarmer().getEmail());
        measurement.setFieldCoord(apiWateringScheduler.getActuator().getField().getCoord());
        measurement.setDuration(apiWateringScheduler.getDuration());
        measurement.setDeviceId(apiWateringScheduler.getActuator().getId());
        measurementRepository.save(measurement);
        return measurement;
    }

}
