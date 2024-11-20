package ru.practicum.mainservice.mapper;

import org.mapstruct.*;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface CommentMapper {

    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    Comment toComment(CommentDto commentDto);

    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toCommentDtoList(List<Comment> comments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateComment(@MappingTarget Comment comment, CommentDto dto);
}
