package ru.practicum.mainservice.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    List<Integer> events = List.of();
    Boolean pinned = false;
    @NotBlank
    String title;

}
