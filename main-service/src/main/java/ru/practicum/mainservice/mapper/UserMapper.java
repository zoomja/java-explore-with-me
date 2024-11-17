package ru.practicum.mainservice.mapper;

import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    List<UserDto> toUserDtos(List<User> users);
}
