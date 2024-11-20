package ru.practicum.mainservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.dto.request.ParticipationRequestDto;
import ru.practicum.mainservice.model.Request;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "event", ignore = true)
    ParticipationRequestDto toParticipationRequestDto(Request request);

    List<ParticipationRequestDto> toParticipationRequestDtoList(List<Request> requests);

}
