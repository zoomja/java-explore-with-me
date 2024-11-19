package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    List<UserDto> toUserDtos(List<User> users);

    User toUserRequest(NewUserRequest newUserRequest);
}
