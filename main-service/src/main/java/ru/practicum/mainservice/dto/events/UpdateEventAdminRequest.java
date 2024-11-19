package ru.practicum.mainservice.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainservice.model.Location;
import ru.practicum.mainservice.model.enums.EventStateAction;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    String annotation;
    Integer category;
    @Size(min = 20, max = 7000)
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    LocalDateTime eventDate;
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    EventStateAction stateAction;
    @Size(min = 3, max = 120)
    String title;
}
