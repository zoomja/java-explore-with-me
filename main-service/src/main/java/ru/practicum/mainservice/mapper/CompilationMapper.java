package ru.practicum.mainservice.mapper;

import org.mapstruct.*;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationRequest;
import ru.practicum.mainservice.model.Compilation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    List<CompilationDto> toDtos(List<Compilation> compilations);

    CompilationDto toDto(Compilation compilation);

    @Mapping(target = "events", ignore = true)
    Compilation toEntity(NewCompilationDto newcompilationDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "events", ignore = true)
    void updateCompilation(@MappingTarget Compilation compilation, UpdateCompilationRequest dto);
}
