package ru.practicum.mainservice.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 10, max = 50)
    String title;

}
