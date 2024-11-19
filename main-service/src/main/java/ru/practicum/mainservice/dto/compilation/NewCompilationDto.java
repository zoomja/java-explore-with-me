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

    private List<Integer> events = List.of();
    private Boolean pinned = false;
    @NotBlank
    @Size(min = 10, max = 50)
    private String title;

}
