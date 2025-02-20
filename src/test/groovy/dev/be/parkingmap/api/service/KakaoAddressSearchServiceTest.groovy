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

    def "정상적인 주소를 입력했을 경우, 정상적으로 위도,경도로 변환 된다." (){
        given:
        boolean actualResult = false;

        when:
        def searchResult = kakaoAddressSearchService.requestAddressSearch(inputAddress);

        then:
        if (searchResult == null) actualResult = false;
        else searchResult.getDocumentList().size() > 0

        where:
        inputAddress                            | expectedResult
        "인천광역시 부평구 삼산동"                   | true
        "인천광역시 부평구 삼산동 453-1"             | true
        "인천 길주로"                              | true
        "인천 부평구 삼산동 잘못된주소"               | false
        "부평구 삼산동 453-2"                      | true
        "부평구 삼산동 4215-521152"                | false
        ""                                      | false
    }
}