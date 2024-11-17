package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    Request findRequestByRequesterIdAndEventId(Integer requesterId, Integer eventId);

    List<Request> findAllByRequester_Id(Integer userId);

    Boolean existsParticipationRequestByRequester_idAndEvent_Id(Integer userId, Integer eventId);
}
