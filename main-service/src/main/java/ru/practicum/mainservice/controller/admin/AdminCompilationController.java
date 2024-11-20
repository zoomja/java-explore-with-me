package ru.practicum.mainservice.controller.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationRequest;
import ru.practicum.mainservice.service.CompilationService;

@RestController
@RequestMapping("/admin/compilations")
@AllArgsConstructor
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Integer compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(
            @PathVariable Integer compId,
            @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }
}
