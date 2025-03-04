package dev.be.parkingmap.parking.repository

import dev.be.parkingmap.AbstractIntegrationContainerBaseTest
import dev.be.parkingmap.parking.entity.Parking
import org.springframework.beans.factory.annotation.Autowired

import java.lang.reflect.Array
import java.time.LocalDateTime

class ParkingRepositoryTest extends AbstractIntegrationContainerBaseTest {
    @Autowired
    private ParkingRepository parkingRepository;

    def setup(){
        parkingRepository.deleteAll()
    }

    def "ParkingRepository save"(){
        given:
        String address = "인천광역시 부평구 부개동"
        String name = "삼산주차타워"
        double latitude = 35.11
        double longitude = 127.11

        def parking = Parking.builder()
            .parkingAddress(address)
            .parkingName(name)
            .latitude(latitude)
            .longitude(longitude)
            .build()

        when:
        def result = parkingRepository.save(parking)

        then:
        result.getParkingAddress() == address
        result.getParkingName() == name
        result.getLatitude() == latitude
        result.getLongitude() == longitude
    }

    def "ParkingRepository saveAll"(){
        given:
        String address = "인천광역시 부평구 부개동"
        String name = "삼산 주차타워"
        double latitude = 35.11
        double longitude = 127.11

        def parking = Parking.builder()
                .parkingAddress(address)
                .parkingName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        parkingRepository.saveAll(Arrays.asList(parking))
        def result = parkingRepository.findAll()

        then:
        result.size() == 1
    }

    def "BaseTimeEntity 등록"(){
        given:
        LocalDateTime now = LocalDateTime.now()
        String address = "인천광역시 부평구 삼산동"
        String name = "삼산주차타워"

        def parking = Parking.builder()
            .parkingAddress(address)
            .parkingName(name)
            .build()

        when:
        parkingRepository.save(parking)
        def result = parkingRepository.findAll()

        then:
        result.get(0).getCreatedDate().isAfter(now)
        result.get(0).getModifiedDate().isAfter(now)
    }
}