package dev.be.parkingmap.parking.entity;

import dev.be.parkingmap.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "parking")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parking extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String parkingName;
    private String parkingAddress;
    private double latitude;
    private double longitude;

    public void changeParkingAddress(String address) {
        this.parkingAddress = address;
    }
}
