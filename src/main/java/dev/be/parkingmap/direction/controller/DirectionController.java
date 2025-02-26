package dev.be.parkingmap.direction.controller;

import dev.be.parkingmap.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DirectionController {

    private final DirectionService directionService;


}
