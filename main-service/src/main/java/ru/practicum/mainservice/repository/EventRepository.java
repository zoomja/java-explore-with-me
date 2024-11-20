package ru.practicum.mainservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.enums.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findAllByInitiatorId(Integer id, Pageable pageable);

    Event findByIdAndInitiatorId(Integer id, Integer initiatorId);

    Page<Event> findAllByEventDateBeforeAndEventDateAfter(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Event> findAllByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCategory_IdInAndPaidIsAndEventDateBeforeAndEventDateAfter(
            String text,
            String text2,
            List<Integer> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable);

    Event findByIdAndInitiator_Id(Integer id, Integer initiatorId);

    Page<Event> findAllByInitiator_IdInAndStateInAndCategory_IdInAndEventDateBeforeAndEventDateAfter(
            List<Integer> userId, List<State> states, List<Integer> categoryId, LocalDateTime start,
            LocalDateTime end, Pageable pageable);

    Optional<Event> findByIdAndStateIn(Integer eventId, List<State> states);

    List<Event> findAllByIdIn(List<Integer> eventIds);
}
