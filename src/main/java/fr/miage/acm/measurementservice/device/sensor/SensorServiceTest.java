package fr.miage.acm.measurementservice.device.sensor;

import fr.miage.acm.measurementservice.device.DeviceState;
import fr.miage.acm.measurementservice.farmer.Farmer;
import fr.miage.acm.measurementservice.farmer.FarmerService;
import fr.miage.acm.measurementservice.field.Field;
import fr.miage.acm.measurementservice.field.FieldService;
import org.springframework.stereotype.Component;


@Component
public class SensorServiceTest {
    private final SensorService sensorService;
    private final FarmerService farmerService;
    private final FieldService fieldService;

    public SensorServiceTest(SensorService sensorService, FarmerService farmerService, FieldService fieldService) {
        this.sensorService = sensorService;
        this.farmerService = farmerService;
        this.fieldService = fieldService;
    }

    public void getSensors() {
        sensorService.findAll().forEach(sensor -> System.out.println(sensor.toString()));
    }

    public void removeSensors() {
        sensorService.findAll().forEach(sensor -> System.out.println(sensor.toString()));
    }


    public void addSensor() {
        Farmer farmer = farmerService.findByEmail("johndoe@gmail.com");
        sensorService.addSensor(farmer);
    }

    public void assignSensorToField() {
        Farmer farmer = farmerService.findByEmail("johndoe@gmail.com");
        Field field = fieldService.findByFarmer(farmer).get(0);
        Sensor sensor = sensorService.findByFarmer(farmer).get(0);
        sensorService.assignSensorToField(sensor, field);
    }

    public void unassignSensorToField() {
        Farmer farmer = farmerService.findByEmail("johndoe@gmail.com");
        Sensor sensor = sensorService.findByFarmer(farmer).get(0);
        sensorService.unassignSensorFromField(sensor);
    }


    public void removeSensor() {
        Farmer farmer = farmerService.findByEmail("johndoe@gmail.com");
        Sensor sensor = sensorService.findByFarmer(farmer).get(0);
        sensorService.delete(sensor);
    }

    public void changeSensorState() {
        Farmer farmer = farmerService.findByEmail("johndoe@gmail.com");
        Sensor sensor = sensorService.findByFarmer(farmer).get(0);
        sensorService.changeState(sensor, sensor.getState().equals(DeviceState.ON) ? DeviceState.OFF : DeviceState.ON);
        sensorService.save(sensor);
    }

}
