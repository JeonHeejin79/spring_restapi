package com.example.restapiwithspring.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

    @Test
    public void builderTest() {
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {

        // given
        String description = "Spring";
        String name = "Event";

        // when
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        // then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    public void junitVersion() {
        System.out.println(junit.runner.Version.id());
    }

    @Test
    @Parameters({
            "0, 0, true",
            "100, 0, false",
            "0, 100, false"
    })
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        // when
        event.update();
        // then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    @Test
    public void testOffline() {
        // given
        Event event = Event.builder()
                .location("강남역 네이버 D2 스티텁 팩토리")
                .build();
        // when
        event.update();
        // then
        assertThat(event.isOffline()).isTrue();

        // given
        event = Event.builder()
                .build();
        // when
        event.update();
        // then
        assertThat(event.isOffline()).isFalse();
    }
}