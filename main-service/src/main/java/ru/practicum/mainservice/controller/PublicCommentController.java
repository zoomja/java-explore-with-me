package ru.practicum.mainservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.service.CommentService;

import java.util.Map;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comment")
@RequiredArgsConstructor
public class PublicCommentController {
    private final CommentService commentService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@Valid @RequestBody CommentDto commentDto,
                                 @PathVariable Integer userId,
                                 @PathVariable Integer eventId) {
        return commentService.addComment(userId, eventId, commentDto);
    }

    @PatchMapping("{commentId}")
    public CommentDto changeComment(@Valid @RequestBody CommentDto commentDto,
                                    @PathVariable Integer userId,
                                    @PathVariable Integer eventId,
                                    @PathVariable Integer commentId) {
        return commentService.updateComment(userId, eventId, commentId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Map<String, String> deleteComment(@PathVariable Integer userId,
                                             @PathVariable Integer eventId,
                                             @PathVariable Integer commentId) {
        commentService.deleteComment(userId, eventId, commentId);
        return Map.of("message", "Comment with id " + commentId + " deleted");
    }
}
