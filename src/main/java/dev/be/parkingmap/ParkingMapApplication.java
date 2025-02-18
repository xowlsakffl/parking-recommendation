package dev.be.parkingmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ParkingMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkingMapApplication.class, args);
    }

}
