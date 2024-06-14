package fr.miage.acm.measurementservice.farmer;

import fr.miage.acm.measurementservice.api.ApiFarmer;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Farmer {

    private UUID id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Integer fieldSize;

    public Farmer(String firstName, String lastName, String email, String password, int fieldSize) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.fieldSize = fieldSize;
    }

    public Farmer() {
        // Default constructor required by JPA
    }

    public Farmer(ApiFarmer apiFarmer) {
        this.id = apiFarmer.getId();
        this.firstName = apiFarmer.getFirstName();
        this.lastName = apiFarmer.getLastName();
        this.email = apiFarmer.getEmail();
        this.password = apiFarmer.getPassword();
        this.fieldSize = apiFarmer.getFieldSize();
    }

    @Override
    public String toString() {
        return "Farmer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fieldSize=" + fieldSize +
                '}';
    }
}
