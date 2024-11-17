package ru.practicum.mainservice.controller;

import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.service.EventService;
import ru.practicum.mainservice.dto.events.EventFullDto;
import ru.practicum.mainservice.dto.events.EventShortDto;
import ru.practicum.mainservice.dto.events.NewEventDto;
import ru.practicum.mainservice.dto.events.UpdateEventUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.service.RequestService;

import java.util.List;


@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventsController {

    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    public EventFullDto createEvent(@PathVariable Integer userId, @RequestBody NewEventDto newEventDto) {
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> getEvents(
            @PathVariable Integer userId,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getAllEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer userId, @PathVariable Integer eventId, @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return eventService.updateEvent(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public ParticipationRequestDto getRequests(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return requestService.getRequests(userId, eventId);

    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequest(@PathVariable Integer userId, @PathVariable Integer eventId, @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest){
        return requestService.updateRequests(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
