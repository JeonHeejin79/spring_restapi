package com.example.restapiwithspring.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {
    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice()
            && eventDto.getMaxPrice() >= 0) {
            // errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong"); // field error
            // errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong"); // field error
            /** reject -> global error 에 들어가게 된다.*/
            errors.reject("wrongPrices", "Value for Prices are wrong"); // global error
        }

        LocalDateTime endEventDateTime =
                eventDto.getEndEventDateTime();

        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
            endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
            endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            /** rejectValue -> field error 에 들어가게 된다.*/
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
        }

        // TODO BeginEventDateTime

        // TODO CloseEnrollmentDateTime
    }
}
