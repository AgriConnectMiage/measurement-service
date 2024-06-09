package fr.miage.acm.measurementservice.field;

import fr.miage.acm.measurementservice.api.ApiField;
import fr.miage.acm.measurementservice.farmer.Farmer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/fields")
public class FieldController {

    private final FieldService fieldService;

    @Autowired
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @GetMapping
    public List<ApiField> getAllFields() {
        return fieldService.findAll().stream().map(ApiField::new).toList();
    }

    @GetMapping("/{id}")
    public Optional<Field> getFieldById(@PathVariable UUID id) {
        return fieldService.findById(id);
    }

    @GetMapping("/farmer/{farmerId}")
    public List<Field> getFieldsByFarmer(@PathVariable UUID farmerId) {
        Farmer farmer = new Farmer();
        farmer.setId(farmerId);
        return fieldService.findByFarmer(farmer);
    }

    @PostMapping
    public Field createField(@RequestBody Field field) {
        return fieldService.save(field);
    }

    @DeleteMapping("/farmer/{farmerId}")
    public void deleteFieldsByFarmer(@PathVariable UUID farmerId) {
        Farmer farmer = new Farmer();
        farmer.setId(farmerId);
        fieldService.deleteFieldsByFarmer(farmer);
    }
}
