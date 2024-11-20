package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.CommentMapper;
import ru.practicum.mainservice.model.Comment;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final EventService eventService;

    public CommentDto addComment(Integer userId, Integer eventId, CommentDto commentDto) {
        Comment comment = commentMapper.toComment(commentDto);
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        comment.setAuthor(user);
        comment.setEvent(event);
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    public CommentDto updateComment(Integer userId, Integer eventId, Integer commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findByIdAndEvent_IdAndAuthor_Id(commentId, eventId, userId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        commentMapper.updateComment(comment, commentDto);
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    @Transactional
    public void deleteComment(Integer userId, Integer eventId, Integer commentId) {
        if (!commentRepository.existsByIdAndEvent_IdAndAuthor_Id(commentId, eventId, userId)) {
            throw new NotFoundException("For user with id " + userId + " and event with id " + eventId + " not found comment");
        }
        commentRepository.deleteById(commentId);
    }

    public List<CommentDto> getAllComments(Integer eventId) {
        List<Comment> comments = commentRepository.findAllByEvent_Id(eventId);
        return commentMapper.toCommentDtoList(comments);
    }
}
