package dev.be.parkingmap.direction.repository;

import dev.be.parkingmap.direction.entity.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectionRepository extends JpaRepository<Direction, Long> {
}
