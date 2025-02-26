package dev.be.parkingmap.direction.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OutputDto {
    private String parkingName;    // 주차장 명
    private String parkingAddress; // 주차장 주소
    private String directionUrl;    // 길안내 url
    private String roadViewUrl;     // 로드뷰 url
    private String distance;
}
