package dev.be.parkingmap.parking.controller;

import dev.be.parkingmap.parking.cache.ParkingRedisTemplateService;
import dev.be.parkingmap.parking.dto.ParkingDto;
import dev.be.parkingmap.parking.service.ParkingRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingRepositoryService parkingRepositoryService;
    private final ParkingRedisTemplateService parkingRedisTemplateService;

    // 데이터 초기 셋팅을 위한 임시 메서드
    @GetMapping("/redis/save")
    public String save() {

        List<ParkingDto> parkingDtoList = parkingRepositoryService.findAll()
                .stream().map(parking -> ParkingDto.builder()
                        .id(parking.getId())
                        .parkingName(parking.getParkingName())
                        .parkingAddress(parking.getParkingAddress())
                        .latitude(parking.getLatitude())
                        .longitude(parking.getLongitude())
                        .build())
                .collect(Collectors.toList());

        parkingDtoList.forEach(parkingRedisTemplateService::save);

        return "success";
    }
}
