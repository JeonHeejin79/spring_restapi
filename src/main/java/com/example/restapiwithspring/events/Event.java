package com.example.restapiwithspring.events;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder 
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 파라미터를 갖고있는 생성자
@Getter @Setter
@EqualsAndHashCode(of = "id") // id 값만을 갖고 equals 와 hashcode 를 비교하도록 함
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

}
