package dev.be.parkingmap.parking.service;

import dev.be.parkingmap.parking.entity.Parking;
import dev.be.parkingmap.parking.repository.ParkingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingRepositoryService {
    private final ParkingRepository parkingRepository;

    @Transactional
    public void updateAddress(Long id, String address){
        Parking entity = parkingRepository.findById(id).orElse(null);

        if(Objects.isNull(entity)){
            log.error("Parking with id {} not found", id);
            return;
        }

        entity.changeParkingAddress(address);
    }

    public void updateAddressWithoutTransaction(Long id, String address){
        Parking entity = parkingRepository.findById(id).orElse(null);

        if(Objects.isNull(entity)){
            log.error("Parking with id {} not found", id);
            return;
        }

        entity.changeParkingAddress(address);
    }
}
