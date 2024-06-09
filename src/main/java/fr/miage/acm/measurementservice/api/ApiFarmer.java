package fr.miage.acm.measurementservice.api;

import fr.miage.acm.measurementservice.farmer.Farmer;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ApiFarmer {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer fieldSize;

    public ApiFarmer(Farmer farmer) {
        this.id = farmer.getId();
        this.firstName = farmer.getFirstName();
        this.lastName = farmer.getLastName();
        this.email = farmer.getEmail();
        this.fieldSize = farmer.getFieldSize();
    }

    @Override
    public String toString() {
        return "Farmer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", fieldSize=" + fieldSize +
                '}';
    }
}
