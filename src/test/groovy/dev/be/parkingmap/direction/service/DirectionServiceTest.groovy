package dev.be.parkingmap.direction.service

import dev.be.parkingmap.api.dto.DocumentDto
import dev.be.parkingmap.parking.dto.ParkingDto
import dev.be.parkingmap.parking.service.ParkingSearchService
import spock.lang.Specification


class DirectionServiceTest extends Specification {
    private ParkingSearchService parkingSearchService = Mock();

    private DirectionService directionService = new DirectionService(parkingSearchService);

    private List<ParkingDto> parkingList;

    def setup(){
        parkingList = new ArrayList<>()
        parkingList.addAll(
                ParkingDto.builder()
                        .id(1L)
                        .parkingName("삼산4차")
                        .parkingAddress("주소2")
                        .latitude(37.520155)
                        .longitude(126.747619)
                        .build(),
                ParkingDto.builder()
                        .id(2L)
                        .parkingName("삼산5차")
                        .parkingAddress("주소1")
                        .latitude(37.520211)
                        .longitude(126.748384)
                        .build()
        )
    }

    def "buildDirectionList - 결과 값이 거리순으로 정렬이 되는지 확인" (){
        given:
        def addressName = "인천 부평구 충선로 191"
        double inputLatitude = 37.506344
        double inputLongitude = 126.7359677

        def documentDto = DocumentDto.builder()
            .addressName(addressName)
            .latitude(inputLatitude)
            .longitude(inputLongitude)
            .build()

        when:
        parkingSearchService.searchParkingDtoList() >> parkingList
        def results = directionService.buildDirectionList(documentDto)

        then:
        results.size() == 2
        results.get(0).targetParkingName == "삼산4차"
        results.get(1).targetParkingName == "삼산5차"
    }

    def "buildDirectionList - 정해진 반경 10km 내에 검색이 되는지 확인" (){
        given:
        parkingList.add(
                ParkingDto.builder()
                        .id(3L)
                        .parkingName("10km 밖 어딘가")
                        .parkingAddress("주소1")
                        .latitude(37.6465654)
                        .longitude(126.86867867)
                        .build()
        )

        def addressName = "인천 부평구 충선로 191"
        double inputLatitude = 37.506344
        double inputLongitude = 126.7359677

        def documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build()

        when:
        parkingSearchService.searchParkingDtoList() >> parkingList
        def results = directionService.buildDirectionList(documentDto)

        then:
        results.size() == 2
        results.get(0).targetParkingName == "삼산4차"
        results.get(1).targetParkingName == "삼산5차"
    }
}