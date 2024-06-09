package fr.miage.acm.measurementservice.device;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "device_id", insertable = false, updatable = false)
    private Device device;

    private Integer humidity;
    private Integer temperature;
    private Integer duration;

    public Measurement(UUID id, LocalDateTime dateTime, Device device, Integer humidity, Integer temperature, Integer duration) {
        this.id = id;
        this.dateTime = dateTime;
        this.device = device;
        this.humidity = humidity;
        this.temperature = temperature;
        this.duration = duration;
    }

    public Measurement() {
        // Default constructor required by JPA
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", source=" + device +
                ", humidity=" + humidity +
                ", temperature=" + temperature +
                ", duration=" + duration +
                '}';
    }
}
