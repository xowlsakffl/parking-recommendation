package dev.be.parkingmap.api.service

import spock.lang.Specification

import java.nio.charset.StandardCharsets


class KakaoUriBuilderServiceTest extends Specification {
    private KakaoUriBuilderService kakaoUriBuilderService;

    def setup() {
        kakaoUriBuilderService = new KakaoUriBuilderService();
    }

    def "buildUriByAddressSearch - 한글 파라미터의 경우 정상적으로 인코딩"() {
        given:
        String address = "인천 부평구"
        def charset = StandardCharsets.UTF_8

        when:
        def uri = kakaoUriBuilderService.buildUriByAddressSearch(address)
        def decodedResult = URLDecoder.decode(uri.toString(), charset);

        then:
        println uri
        decodedResult == "https://dapi.kakao.com/v2/local/search/address.json?query=인천 부평구"
    }
}