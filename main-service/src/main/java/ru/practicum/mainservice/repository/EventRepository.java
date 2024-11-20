package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainservice.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.enums.State;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findAllByInitiatorId(Integer id, Pageable pageable);

    Event findByIdAndInitiatorId(Integer id, Integer initiatorId);

    Page<Event> findAllByEventDateBeforeAndEventDateAfter(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE LOWER(e.annotation) LIKE LOWER(:text) " +
            "OR LOWER(e.description) LIKE LOWER(:text2) " +
            "AND e.category.id IN (:categories) " +
            "AND e.paid = :paid " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    Page<Event> findAllByCriteria(
            @Param("text") String text,
            @Param("text2") String text2,
            @Param("categories") List<Integer> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    Event findByIdAndInitiator_Id(Integer id, Integer initiatorId);

    Page<Event> findAllByInitiator_IdInAndStateInAndCategory_IdInAndEventDateBeforeAndEventDateAfter(
            List<Integer> userId, List<State> states, List<Integer> categoryId, LocalDateTime start,
            LocalDateTime end, Pageable pageable);

    Optional<Event> findByIdAndStateIn(Integer eventId, List<State> states);

    List<Event> findAllByIdIn(List<Integer> eventIds);
}
