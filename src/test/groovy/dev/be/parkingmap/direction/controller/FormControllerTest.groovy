package dev.be.parkingmap.direction.controller

import dev.be.parkingmap.direction.dto.OutputDto
import dev.be.parkingmap.parking.service.ParkingRecommendationService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class FormControllerTest extends Specification {

    private MockMvc mockMvc;
    private ParkingRecommendationService parkingRecommendationService = Mock();
    private List<OutputDto> outputDtoList;

    def setup(){
        // FormController MockMvc 객체를 생성
        mockMvc = MockMvcBuilders.standaloneSetup(new FormController(parkingRecommendationService))
            .build();

        outputDtoList = new ArrayList<>()
        outputDtoList.addAll(
                OutputDto.builder()
                        .parkingName("parking1")
                        .build(),
                OutputDto.builder()
                        .parkingName("parking2")
                        .build()
        )
    }

    def "GET /" (){
        expect:
        // FormController 의 "/" URI를 get방식으로 호출
        mockMvc.perform(get("/"))
                .andExpect(handler().handlerType(FormController.class))
                .andExpect(handler().methodName("main"))
                .andExpect(status().isOk()) // 예상 값을 검증한다.
                .andExpect(view().name("main"))
                .andDo(log())
    }

    def "POST /search"() {

        given:
        String inputAddress = "인천 부평구 부개동"

        when:
        def resultActions = mockMvc.perform(post("/search")
                .param("address", inputAddress))

        then:
        1 * parkingRecommendationService.recommendParkingList(argument -> {
            assert argument == inputAddress // mock 객체의 argument 검증
        }) >> outputDtoList

        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("output"))
                .andExpect(model().attributeExists("outputFormList")) // model에 outputFormList라는 key가 존재하는지 확인
                .andExpect(model().attribute("outputFormList", outputDtoList))
                .andDo(print())
    }
}