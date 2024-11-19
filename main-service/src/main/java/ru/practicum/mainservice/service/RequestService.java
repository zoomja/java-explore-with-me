package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.RequestMapper;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.Request;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.model.enums.State;
import ru.practicum.mainservice.repository.EventRepository;
import ru.practicum.mainservice.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserService userService;
    private final EventRepository eventRepository;

    public List<ParticipationRequestDto> getRequests(Integer userId, Integer eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        List<Request> requests;
        if (event.getInitiator().getId().equals(userId)) {
            requests = requestRepository.findAllByEvent_Id(eventId);
        } else {
            requests = requestRepository.findAllByRequester_IdAndEvent_Id(userId, eventId);
        }
        return updateDtos(requests);
    }

    public EventRequestStatusUpdateResult updateRequests(Integer userId, Integer eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId);
        List<Request> requests = requestRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();

        if (requests.stream().filter(r -> r.getStatus().equals(State.PENDING)).toList().isEmpty()) {
            throw new ConflictException("Requests not pending yet");
        }

        if (event.getConfirmedRequests() != null && event.getParticipantLimit() != 0
                && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ConflictException("The participant limit has been reached");
        }

        if (eventRequestStatusUpdateRequest.getStatus() == State.CONFIRMED) {
            eventRequestStatusUpdateResult.setConfirmedRequests(createParticipationRequestDto(requests, State.CONFIRMED));
            event.setConfirmedRequests(event.getConfirmedRequests() + requests.size());
        } else {
            eventRequestStatusUpdateResult.setRejectedRequests(createParticipationRequestDto(requests, State.REJECTED));
            event.setConfirmedRequests(event.getConfirmedRequests() - requests.size());
        }

        eventRepository.save(event);
        requestRepository.saveAll(requests);

        return eventRequestStatusUpdateResult;
    }


    public List<ParticipationRequestDto> getRequests(Integer userId) {
        userService.getUserById(userId);
        List<Request> requests = requestRepository.findAllByRequester_Id(userId);
        return updateDtos(requests);
    }

    public ParticipationRequestDto addRequest(Integer userId, Integer eventId) {
        if (requestRepository.existsParticipationRequestByRequester_idAndEvent_Id(userId, eventId)) {
            throw new ConflictException("Запрос уже существует");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event не найден"));

        if (event.getInitiator().getId().equals(userId) || event.getState() == State.PENDING ||
                event.getState() == State.CANCELED) {
            throw new ConflictException("Нельзя участвовать в своем или неопубликованном событии");
        }

//        if (!event.getRequestModeration()) {
        if (event.getConfirmedRequests() != null && event.getParticipantLimit() != 0
                && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ConflictException("The participant limit has been reached");
        }
//        }

        User requester = userService.getUserById(userId);
        Request request = Request.builder()
                .created(LocalDateTime.now()).requester(requester).event(event).build();

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus((State.CONFIRMED));
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        } else request.setStatus((State.PENDING));

        requestRepository.save(request);
        eventRepository.save(event);


        ParticipationRequestDto participationRequestDto = requestMapper.toParticipationRequestDto(request);
        participationRequestDto.setRequester(request.getRequester().getId());
        participationRequestDto.setEvent(request.getEvent().getId());

        return participationRequestDto;
    }

    public ParticipationRequestDto cancelRequest(Integer userId, Integer requestId) {
        Request request = requestRepository
                .findById(requestId).orElseThrow(() -> new NotFoundException("Запрос не существует"));
        if (!request.getRequester().getId().equals(userId)) {
            throw new NotFoundException("Пользователь не явлется владельцем запроса");
        }
        request.setStatus(State.CANCELED);
        requestRepository.save(request);
        ParticipationRequestDto participationRequestDto = requestMapper.toParticipationRequestDto(request);
        participationRequestDto.setRequester(request.getRequester().getId());
        participationRequestDto.setEvent(request.getEvent().getId());
        return participationRequestDto;
    }

    private List<ParticipationRequestDto> updateDtos(List<Request> requests) {
        List<ParticipationRequestDto> participationRequestDtos = requestMapper.toParticipationRequestDtoList(requests);
        for (int i = 0; i < requests.size(); i++) {
            Request participationRequest = requests.get(i);
            participationRequestDtos.get(i).setEvent(participationRequest.getEvent().getId());
            participationRequestDtos.get(i).setRequester(participationRequest.getRequester().getId());
        }
        return participationRequestDtos;
    }

    private List<ParticipationRequestDto> createParticipationRequestDto(List<Request> requests, State status) {
        List<ParticipationRequestDto> participationRequestDto = new ArrayList<>();
        requests.forEach(
                request -> {
                    request.setStatus(State.CONFIRMED);
                    participationRequestDto.add(
                            ParticipationRequestDto.builder()
                                    .id(request.getId())
                                    .status(status)
                                    .created(request.getCreated())
                                    .requester(request.getRequester().getId())
                                    .event(request.getEvent().getId())
                                    .build());
                });
        return participationRequestDto;
    }
}
