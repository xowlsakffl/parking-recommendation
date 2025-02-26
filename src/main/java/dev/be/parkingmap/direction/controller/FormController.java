package dev.be.parkingmap.direction.controller;

import dev.be.parkingmap.direction.dto.InputDto;
import dev.be.parkingmap.parking.service.ParkingRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class FormController {

    private final ParkingRecommendationService parkingRecommendationService;

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @PostMapping("/search")
    public ModelAndView search(@ModelAttribute InputDto inputDto) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("output");
        modelAndView.addObject("outputFormList", parkingRecommendationService.recommendParkingList(inputDto.getAddress()));

        return modelAndView;
    }
}
