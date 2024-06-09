package fr.miage.acm.measurementservice.field;

import org.springframework.stereotype.Component;

@Component
public class FieldServiceTest {
    private final FieldService fieldService;

    public FieldServiceTest(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    public void getFields() {
        System.out.println("Fields: " + fieldService.findAll());
    }
}
