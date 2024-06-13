package fr.miage.acm.measurementservice.field;

import fr.miage.acm.measurementservice.device.actuator.Actuator;
import fr.miage.acm.measurementservice.device.actuator.ActuatorRepository;
import fr.miage.acm.measurementservice.device.actuator.watering.scheduler.WateringScheduler;
import fr.miage.acm.measurementservice.device.actuator.watering.scheduler.WateringSchedulerClient;
import fr.miage.acm.measurementservice.device.actuator.watering.scheduler.WateringSchedulerService;
import fr.miage.acm.measurementservice.farmer.Farmer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FieldService {

    private FieldRepository fieldRepository;
    private WateringSchedulerService wateringSchedulerService;
    // TODO Bad practice to inject actuator repository here ?
    private ActuatorRepository actuatorRepository;


    public FieldService(FieldRepository fieldRepository, ActuatorRepository actuatorRepository, WateringSchedulerService wateringSchedulerService) {
        this.fieldRepository = fieldRepository;
        this.wateringSchedulerService = wateringSchedulerService;
        this.actuatorRepository = actuatorRepository;
    }

    public List<Field> findAll() {
        return fieldRepository.findAll();
    }

    public Field save(Field field) {
        return fieldRepository.save(field);
    }

    public void delete(Field field) {
        fieldRepository.delete(field);
    }

    public Optional<Field> findById(UUID id) {
        return fieldRepository.findById(id);
    }

    public List<Field> findByFarmer(Farmer farmer) {
        return fieldRepository.findByFarmer(farmer);
    }

    @Transactional
    public void deleteFieldsByFarmer(Farmer farmer) {
        fieldRepository.deleteByFarmer(farmer);
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
