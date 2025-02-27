package dev.be.parkingmap.parking.service;

import dev.be.parkingmap.api.dto.DocumentDto;
import dev.be.parkingmap.api.dto.KakaoApiResponseDto;
import dev.be.parkingmap.api.service.KakaoAddressSearchService;
import dev.be.parkingmap.direction.dto.OutputDto;
import dev.be.parkingmap.direction.entity.Direction;
import dev.be.parkingmap.direction.service.Base62Service;
import dev.be.parkingmap.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingRecommendationService {

    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    private final Base62Service base62Service;

    @Value("${parking.recommendation.base.url}")
    private String baseUrl;

    public List<OutputDto> recommendParkingList(String address){
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())){
            log.error("[ParkingRecommendationService recommendParkingList fail] Input address: {}", address);
            return Collections.emptyList();
        }

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);
        List<Direction> directionList = directionService.buildDirectionList(documentDto);
        //List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);

        return directionService.saveAll(directionList)
                .stream()
                .map(this::convertToOutputDto)
                .collect(Collectors.toList());
    }

    private OutputDto convertToOutputDto(Direction direction){
        return OutputDto.builder()
                .parkingName(direction.getTargetParkingName())
                .parkingAddress(direction.getTargetAddress())
                .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId()))
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude()) // todo
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }
}
