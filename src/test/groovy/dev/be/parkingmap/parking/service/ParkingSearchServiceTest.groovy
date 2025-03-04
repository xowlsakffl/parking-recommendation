package dev.be.parkingmap.parking.service

import com.google.common.collect.Lists
import dev.be.parkingmap.parking.cache.ParkingRedisTemplateService
import dev.be.parkingmap.parking.dto.ParkingDto
import dev.be.parkingmap.parking.entity.Parking
import spock.lang.Specification


class ParkingSearchServiceTest extends Specification {
    private ParkingSearchService parkingSearchService;

    private ParkingRepositoryService parkingRepositoryService = Mock();
    private ParkingRedisTemplateService parkingRedisTemplateService = Mock();

    private List<Parking> parkingList

    def setup(){
        parkingSearchService = new ParkingSearchService(parkingRepositoryService, parkingRedisTemplateService);

        parkingList = Lists.newArrayList(
                Parking.builder()
                        .id(1L)
                        .parkingName("삼산4차")
                        .latitude(37.520155)
                        .longitude(126.747619)
                        .build(),
                Parking.builder()
                        .id(2L)
                        .parkingName("삼산5차")
                        .latitude(37.520211)
                        .longitude(126.748384)
                        .build()
        )
    }

    def "레디스 장애시 DB를 이용하여 약국 데이터 조회"(){
        when:
        parkingRedisTemplateService.findAll() >> []
        parkingRepositoryService.findAll() >> parkingList

        def result = parkingSearchService.searchParkingDtoList()

        then:
        result.size() == 2
    }
}