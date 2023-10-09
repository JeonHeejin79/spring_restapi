package com.example.restapiwithspring.events;

import com.example.restapiwithspring.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest // 웹과 관련된 빈들만 등록
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc; // 웹서버를 띄우지 않기 떄문에 조금더 빠르다. dispatcher servlet 을 만들기 떄문에 단위테스트보다는 조금 더 걸린다.

    @Autowired
    ObjectMapper objectMapper;

    // api.json  api.xml
    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 9, 7, 8, 10))
                .closeEnrollmentDateTime(LocalDateTime.of(2023,9,8,7,8,10))
                .beginEventDateTime(LocalDateTime.of(2023,9,9,7,8,10))
                .endEventDateTime(LocalDateTime.of(2023,9,10,7,8,10))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타터 팩토리")
                .build();

        // event.setId(10);
        // Mockito.when(eventRepository.save(event)).thenReturn(event); // mocking 을 했을때 파라미터에 Event 객체가 들었갔기 떄문에 null 이 리턴됐다.

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));
    }


    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 9, 7, 8, 10))
                .closeEnrollmentDateTime(LocalDateTime.of(2023,9,8,7,8,10))
                .beginEventDateTime(LocalDateTime.of(2023,9,9,7,8,10))
                .endEventDateTime(LocalDateTime.of(2023,9,10,7,8,10))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타터 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        // event.setId(10);
        // Mockito.when(eventRepository.save(event)).thenReturn(event); // mocking 을 했을때 파라미터에 Event 객체가 들었갔기 떄문에 null 이 리턴됐다.

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 9, 8, 8, 10))
                .closeEnrollmentDateTime(LocalDateTime.of(2023,9,7,7,8,10))
                .beginEventDateTime(LocalDateTime.of(2023,9,11,7,8,10))
                .endEventDateTime(LocalDateTime.of(2023,9,10,7,8,10))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타터 팩토리")
                .build()
                ;

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                // .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].fieldValue").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                // .andExpect(jsonPath("$[0].code").exists())
                // .andExpect(jsonPath("$[0].rejectedValue").exists())
        ;
    }
}
