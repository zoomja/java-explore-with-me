package ru.practicum.mainservice.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.events.EventFullDto;
import ru.practicum.mainservice.service.CommentService;
import ru.practicum.mainservice.service.EventService;
import ru.practicum.mainservice.dto.events.EventShortDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
    private final EventService eventService;
    private final CommentService commentService;

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request.getRemoteAddr());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Integer eventId, HttpServletRequest request) {
        return eventService.getPublicEvent(eventId, request.getRemoteAddr());
    }

    @GetMapping("/{eventId}/comments")
    public List<CommentDto> getAllComments(@PathVariable Integer eventId) {
        return commentService.getAllComments(eventId);
    }
}
