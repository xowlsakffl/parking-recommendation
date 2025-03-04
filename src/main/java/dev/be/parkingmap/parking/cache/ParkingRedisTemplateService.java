package dev.be.parkingmap.parking.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.be.parkingmap.parking.dto.ParkingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingRedisTemplateService {

    private static final String CACHE_KEY = "PARKING";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private HashOperations<String, String, String> hashOperations;

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void save(ParkingDto parkingDto) {
        if(Objects.isNull(parkingDto) || Objects.isNull(parkingDto.getId())) {
            log.error("Required Values must not be null");
            return;
        }

        try {
            hashOperations.put(CACHE_KEY,
                    parkingDto.getId().toString(),
                    serializeParkingDto(parkingDto));
            log.info("[ParkingRedisTemplateService save success] id: {}", parkingDto.getId());
        } catch (Exception e) {
            log.error("[ParkingRedisTemplateService save error] {}", e.getMessage());
        }
    }

    public List<ParkingDto> findAll() {

        try {
            List<ParkingDto> list = new ArrayList<>();
            for (String value : hashOperations.entries(CACHE_KEY).values()) {
                ParkingDto parkingDto = deserializeParkingDto(value);
                list.add(parkingDto);
            }
            return list;

        } catch (Exception e) {
            log.error("[ParkingRedisTemplateService findAll error]: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public void delete(Long id) {
        hashOperations.delete(CACHE_KEY, String.valueOf(id));
        log.info("[ParkingRedisTemplateService delete]: {} ", id);
    }

    private String serializeParkingDto(ParkingDto parkingDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(parkingDto);
    }

    private ParkingDto deserializeParkingDto(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, ParkingDto.class);
    }
}
