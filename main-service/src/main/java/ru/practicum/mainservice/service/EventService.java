package ru.practicum.mainservice.service;

import client.StatsClient;
import dto.RequestDto;
import ru.practicum.mainservice.dto.events.*;
import ru.practicum.mainservice.mapper.EventMapper;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.exception.ForbiddenException;
import ru.practicum.mainservice.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import ru.practicum.mainservice.model.Category;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.Location;
import ru.practicum.mainservice.model.enums.State;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.LocationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private static final String SERVICE_NAME = "ewm-main-service";

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    private final UserService userService;
    private final CategoryService categoryService;

    private final LocationRepository locationRepository;

    private final StatsClient statsClient;

    public EventFullDto createEvent(Integer userId, NewEventDto newEventDto) {
        checkEventTime(newEventDto.getEventDate());

        User user = userService.getUserById(userId);
        Category category = categoryService.findCategoryById(newEventDto.getCategory());
        Location location = new Location();
        location.setLon(newEventDto.getLocation().getLon());
        location.setLat(newEventDto.getLocation().getLat());
        locationRepository.save(location);

        Event event = eventMapper.toEvent(newEventDto);
        event.setInitiator(user);
        event.setCategory(category);
        event.setLocation(location);
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        event.setConfirmedRequests(0);

        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }


    public List<EventShortDto> getAllEvents(Integer userId, Integer from, Integer size) {
        List<Event> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size)).getContent();
        return eventMapper.toEventShortDtos(events);
    }

    public EventFullDto getEvent(Integer userId, Integer eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        if (event == null) throw new NotFoundException("Эвент не найден");
        return eventMapper.toEventFullDto(event);
    }

    public EventFullDto updateEvent(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Эвент не найден"));
        if (event.getState().equals(State.CANCELLED) || event.getState().equals(State.PENDING)) {
            throw new ForbiddenException("Нельзя изменть эвент в статусе CANCELLED и PENDING");
        }
        if (!event.getInitiator().getId().equals(userId))
            throw new ForbiddenException("Пользователь не создавал этот эвент");
        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(categoryService.findCategoryById(updateEventUserRequest.getCategory()));
        }
        checkEventTime(event.getEventDate());
        eventMapper.update(event, updateEventUserRequest);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    public List<EventShortDto> getEvents(
            String text,
            List<Integer> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size,
            String ip
    ) {
        List<Event> events;

        addNewHit("/events", ip);

        LocalDateTime start = Optional.ofNullable(rangeStart).orElseGet(LocalDateTime::now);
        LocalDateTime end = Optional.ofNullable(rangeEnd).orElseGet(() -> LocalDateTime.now().plusYears(10));

        if (text != null && paid != null && sort != null && categories != null) {
            events = eventRepository.findAllByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCategory_IdInAndPaidIsAndEventDateBeforeAndEventDateAfter(
                            text, text, categories, paid, end, start, PageRequest.of(from / size, size))
                    .getContent();
        } else {
            events = eventRepository.findAllByEventDateBeforeAndEventDateAfter(end, start, PageRequest.of(from / size, size)).getContent();
        }

        if (onlyAvailable)
            return eventMapper.toEventShortDtos(events.stream().filter(e -> e.getParticipantLimit() > 0).toList());

        return eventMapper.toEventShortDtos(events);
    }

    private void addNewHit(String uri, String ip) {
        RequestDto requestDto = RequestDto.builder()
                .app(SERVICE_NAME)
                .uri(uri)
                .timestamp(LocalDateTime.now())
                .ip(ip)
                .build();
        statsClient.saveStatistic(requestDto);
    }

    private void checkEventTime(LocalDateTime eventTime) {
        if (LocalDateTime.now().plusHours(2).isAfter(eventTime))
            throw new ForbiddenException("Неправильно указана дата");
    }

    public List<EventFullDto> getEvents(
            List<Integer> users,
            List<State> states,
            List<Integer> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    ) {
        List<Event> events;
        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
            rangeEnd = rangeStart.plusYears(10);
        }
        if (users != null && states != null && categories != null) {
            events = eventRepository
                    .findAllByInitiator_IdInAndStateInAndCategory_IdInAndEventDateBeforeAndEventDateAfter(
                            users, states, categories, rangeEnd, rangeStart, PageRequest.of(from / size, size))
                    .getContent();
        } else {
            events = eventRepository.findAllByEventDateBeforeAndEventDateAfter(rangeEnd, rangeStart,
                    PageRequest.of(from / size, size)).getContent();
        }
        return eventMapper.toEventFullDtos(events);
    }

    public EventFullDto updateEvent(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Эвент не найден"));
        if (event.getState().equals(State.CANCELLED) || event.getState().equals(State.PENDING)) {
            throw new ForbiddenException("Нельзя изменть эвент в статусе CANCELLED и PENDING");
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryService.findCategoryById(updateEventAdminRequest.getCategory()));
        }
        checkEventTime(event.getEventDate());
        eventMapper.update(event, updateEventAdminRequest);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    public EventFullDto getEvent(Integer eventId, String ip) {
        Event event = eventRepository.findByIdAndStateIn(eventId, List.of(State.PUBLISHED)).orElseThrow(() -> new NotFoundException("Event не найден"));
        if (event.getViews() == null) event.setViews(1);
        else event.setViews(event.getViews() + 1);
        addNewHit("/events/" + eventId, ip);

        return eventMapper.toEventFullDto(event);
    }
}
