package dev.be.parkingmap.parking.repository;

import dev.be.parkingmap.parking.entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingRepository extends JpaRepository<Parking, Long> {
}
