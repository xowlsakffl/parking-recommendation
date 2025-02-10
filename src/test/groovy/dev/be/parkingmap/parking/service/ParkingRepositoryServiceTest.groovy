package dev.be.parkingmap.parking.service

import dev.be.parkingmap.AbstractIntegrationContainerBaseTest
import dev.be.parkingmap.parking.entity.Parking
import dev.be.parkingmap.parking.repository.ParkingRepository
import org.springframework.beans.factory.annotation.Autowired

class ParkingRepositoryServiceTest extends AbstractIntegrationContainerBaseTest{

    @Autowired
    private ParkingRepositoryService parkingRepositoryService;

    @Autowired
    private ParkingRepository parkingRepository;

    def setup(){
        parkingRepository.deleteAll()
    }

    def "ParkingRepository update - dirty checking success" (){
        given:
        String inputAddress = "인천광역시 부평구 부개동"
        String modifiedAddress = "인천광역시 부평구 삼산동"
        String name = "삼산 주차타워"

        def parking = Parking.builder()
                .parkingAddress(inputAddress)
                .parkingName(name)
                .build()

        when:
        def entity = parkingRepository.save(parking)
        parkingRepositoryService.updateAddress(entity.getId(), modifiedAddress)

        def result = parkingRepository.findAll()

        then:
        result.get(0).getParkingAddress() == modifiedAddress

    }
}
