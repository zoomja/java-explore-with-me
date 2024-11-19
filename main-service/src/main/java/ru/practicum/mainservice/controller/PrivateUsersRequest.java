package ru.practicum.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateUsersRequest {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getRequests(@PathVariable Integer userId) {
        return requestService.getRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable Integer userId, @RequestParam Integer eventId) {
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Integer userId, @PathVariable Integer requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
