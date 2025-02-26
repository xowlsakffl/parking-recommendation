package dev.be.parkingmap.parking.service;

import dev.be.parkingmap.api.dto.DocumentDto;
import dev.be.parkingmap.api.dto.KakaoApiResponseDto;
import dev.be.parkingmap.api.service.KakaoAddressSearchService;
import dev.be.parkingmap.direction.dto.OutputDto;
import dev.be.parkingmap.direction.entity.Direction;
import dev.be.parkingmap.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

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
        String params = String.join(",", direction.getTargetParkingName(),
                String.valueOf(direction.getTargetLatitude()), String.valueOf(direction.getTargetLongitude()));

        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params)
                .toUriString();

        log.info("direction params : {}, url : {}", params, result);

        return OutputDto.builder()
                .parkingName(direction.getTargetParkingName())
                .parkingAddress(direction.getTargetAddress())
                .directionUrl(result)
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude()) // todo
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }
}
