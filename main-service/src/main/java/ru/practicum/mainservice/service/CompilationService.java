package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationRequest;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.CompilationMapper;
import ru.practicum.mainservice.model.Compilation;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.repository.CompilationRepository;
import ru.practicum.mainservice.repository.EventRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;


    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations;
        System.out.println();
        if (pinned == null) {
            compilations = compilationRepository.findAll(PageRequest.of(from / size, size)).getContent();
        } else {
            compilations = compilationRepository
                    .findCompilationsByPinnedTrue(PageRequest.of(from / size, size)).getContent();

        }
        return compilationMapper.toDtos(compilations);
    }

    public CompilationDto getCompilation(Integer compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NoSuchElementException("No compilation with id " + compId));
        return compilationMapper.toDto(compilation);
    }

    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.toEntity(newCompilationDto);
        if (newCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        compilationRepository.save(compilation);
        return compilationMapper.toDto(compilation);
    }

    public void deleteCompilation(Integer compId) {
        if (!compilationRepository.existsById(compId)) throw new NotFoundException("Подборка не найдена");
        compilationRepository.deleteById(compId);
    }

    public CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Подборка не найдена"));
        compilationMapper.updateCompilation(compilation, updateCompilationRequest);
        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIn(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
        compilationRepository.save(compilation);
        return compilationMapper.toDto(compilation);
    }
}
