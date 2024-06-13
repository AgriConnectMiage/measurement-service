package fr.miage.acm.measurementservice.field;

import fr.miage.acm.measurementservice.api.ApiField;
import fr.miage.acm.measurementservice.farmer.Farmer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Integer xcoord;
    private Integer ycoord;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    public Field(Integer xcoord, Integer ycoord, Farmer farmer) {
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.farmer = farmer;
    }

    public Field() {
        // Default constructor required by JPA
    }

    public Field(ApiField apiField) {
        this.id = apiField.getId();
        this.xcoord = apiField.getXcoord();
        this.ycoord = apiField.getYcoord();
        this.farmer = new Farmer(apiField.getFarmer());
    }

    @Override
    public String toString() {
        return "Field{" +
                "id=" + id +
                ", xcoord=" + xcoord +
                ", ycoord=" + ycoord +
                ", farmer=" + farmer +
                '}';
    }

    public String getCoord() {
        return xcoord + "," + ycoord;
    }
}
