package dev.be.parkingmap.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingDto {
    private Long id;

    private String parkingName;
    private String parkingAddress;
    private double latitude;
    private double longitude;
}
