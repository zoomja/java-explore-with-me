package ru.practicum.mainservice.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainservice.model.Location;
import ru.practicum.mainservice.model.enums.StateAction;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Integer category;
    @Size(min = 20, max = 7000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @Positive
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    @Size(min = 3, max = 120)
    private String title;
}