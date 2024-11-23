package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findByIdAndEvent_IdAndAuthor_Id(Integer commentId, Integer eventId, Integer authorId);

    Boolean existsByIdAndEvent_IdAndAuthor_Id(Integer commentId, Integer eventId, Integer authorId);

    List<Comment> findAllByEvent_Id(Integer eventId);
}
