package dev.be.parkingmap.parking.service;

import dev.be.parkingmap.parking.dto.ParkingDto;
import dev.be.parkingmap.parking.entity.Parking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParkingSearchService {
    private final ParkingRepositoryService parkingRepositoryService;

    public List<ParkingDto> searchParkingDtoList() {
        // redis

        // db
        return parkingRepositoryService.findAll()
                .stream()
                .map(this::convertToParkingDto)
                .collect(Collectors.toList());
    }

    private ParkingDto convertToParkingDto(Parking parking) {
        return ParkingDto.builder()
                .id(parking.getId())
                .parkingAddress(parking.getParkingAddress())
                .parkingName(parking.getParkingName())
                .latitude(parking.getLatitude())
                .longitude(parking.getLongitude())
                .build();
    }
}
