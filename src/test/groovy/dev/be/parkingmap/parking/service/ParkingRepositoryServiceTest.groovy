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

    def "self invocation"() {

        given:
        String address = "인천광역시 부평구 삼산동"
        String name = "삼산주차타워"
        double latitude = 36.11
        double longitude = 128.11

        def parking = Parking.builder()
                .parkingAddress(address)
                .parkingName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        parkingRepositoryService.bar(Arrays.asList(parking))

        then:
        def e = thrown(RuntimeException.class)
        def result = parkingRepositoryService.findAll()
        result.size() == 1 // 트랜잭션이 적용되지 않는다( 롤백 적용 X )
    }

    def "transactional readOnly test"() {

        given:
        String inputAddress = "인천광역시 부평구 삼산동"
        String modifiedAddress = "인천광역시 부평구 부개동"
        String name = "부개타워"
        double latitude = 36.11
        double longitude = 128.11

        def input = Parking.builder()
                .parkingAddress(inputAddress)
                .parkingName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        def parking = parkingRepository.save(input)
        parkingRepositoryService.startReadOnlyMethod(parking.id)

        then:
        def result = parkingRepositoryService.findAll()
        result.get(0).getParkingAddress() == inputAddress
    }
}
