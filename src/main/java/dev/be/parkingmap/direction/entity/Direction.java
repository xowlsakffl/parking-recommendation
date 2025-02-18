package dev.be.parkingmap.direction.entity;

import dev.be.parkingmap.BaseTimeEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity(name = "direction")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Direction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String inputAddress;
    private double inputLatitude;
    private double inputLongitude;

    private String targetParkingName;
    private String targetAddress;
    private double targetLatitude;
    private double targetLongitude;

    private double distance;
}