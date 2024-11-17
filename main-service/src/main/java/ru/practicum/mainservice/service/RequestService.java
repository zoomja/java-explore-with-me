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
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserService userService;
    private final EventRepository eventRepository;

    public ParticipationRequestDto getRequests(Integer userId, Integer eventId){

        Request request = requestRepository.findRequestByRequesterIdAndEventId(userId, eventId);
        User user = userService.getUserById(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        ParticipationRequestDto participationRequestDto = requestMapper.toParticipationRequestDto(request);
        participationRequestDto.setEvent(event.getId());
        participationRequestDto.setRequester(user.getId());
        participationRequestDto.setStatus(request.getStatus());

        return participationRequestDto;
    }

    public EventRequestStatusUpdateResult updateRequests(Integer userId, Integer eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest){

        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId);
        List<Request> requests = requestRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        if (eventRequestStatusUpdateRequest.getStatus() == State.CONFIRMED){
            List<ParticipationRequestDto> participationRequestDtos = setRequestStatus(requests, State.CONFIRMED);
            participationRequestDtos.forEach(p -> p.setRequester(userId));
            participationRequestDtos.forEach(p -> p.setEvent(eventId));
            eventRequestStatusUpdateResult.setConfirmedRequests(participationRequestDtos);
            event.setConfirmedRequests(event.getConfirmedRequests() + requests.size());
        } else {
            List<ParticipationRequestDto> participationRequestDtos = setRequestStatus(requests, State.REJECTED);
            participationRequestDtos.forEach(p -> p.setRequester(userId));
            participationRequestDtos.forEach(p -> p.setEvent(eventId));
            eventRequestStatusUpdateResult.setRejectedRequests(participationRequestDtos);
            event.setConfirmedRequests(event.getConfirmedRequests() - requests.size());
        }

        eventRepository.save(event);
        requestRepository.saveAll(requests);

        return eventRequestStatusUpdateResult;
    }

    public List<ParticipationRequestDto> getRequests(Integer userId){
        userService.getUserById(userId);
        List<Request> requests = requestRepository.findAllByRequester_Id(userId);
        return requestMapper.toParticipationRequestDtoList(requests);
    }

    public ParticipationRequestDto addRequest(Integer userId, Integer eventId) {
        if (requestRepository.existsParticipationRequestByRequester_idAndEvent_Id(userId, eventId)) {
            throw new ConflictException("Запрос уже существует");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event не найден"));
        if (event.getConfirmedRequests() != null && event.getParticipantLimit() != 0
                && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ConflictException("The participant limit has been reached");
        }
        if (event.getInitiator().getId().equals(userId) || event.getState() == State.PENDING ||
        event.getState() == State.CANCELLED) {
            throw new ConflictException("Нельзя участвовать в своем или неопубликованном событии");
        }
        User requester = userService.getUserById(userId);
        Request request = Request.builder()
                .created(LocalDateTime.now()).requester(requester).event(event).build();
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus((State.CONFIRMED));
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        } else request.setStatus((State.PENDING));
        requestRepository.save(request);
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
        request.setStatus(State.CANCELLED);
        requestRepository.save(request);
        ParticipationRequestDto participationRequestDto = requestMapper.toParticipationRequestDto(request);
        participationRequestDto.setRequester(request.getRequester().getId());
        participationRequestDto.setEvent(request.getEvent().getId());
        return participationRequestDto;
    }

    private List<ParticipationRequestDto> setRequestStatus(List<Request> requests, State status) {
        requests.forEach(r -> r.setStatus(status));
        return requestMapper.toParticipationRequestDtoList(requests);
    }
}
