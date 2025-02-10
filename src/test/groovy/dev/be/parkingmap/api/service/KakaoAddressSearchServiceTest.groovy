package dev.be.parkingmap.api.service

import dev.be.parkingmap.AbstractIntegrationContainerBaseTest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification


class KakaoAddressSearchServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private KakaoAddressSearchService kakaoAddressSearchService;

    def "address 파라미터 값이 null이면 requestAddressSearch 메소드는 null을 리턴한다" (){
        given:
        def address = null;

        when:
        def result = kakaoAddressSearchService.requestAddressSearch(address)

        then:
        result == null
    }

    def "주소값이 valid하다면 requestAddressSearch 메소드는 정상적으로 document를 반환한다"(){
        given:
        def address = "인천 부평구 충선로 191"

        when:
        def result = kakaoAddressSearchService.requestAddressSearch(address)

        then:
        result.documentList.size() > 0
        result.metaDto.totalCount > 0
        result.documentList.get(0).addressName != null
    }
}