package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.UserMapper;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size) {
        if (ids == null) {
            List<User> users = userRepository.findAll(PageRequest.of(from / size, size)).getContent();
            return userMapper.toUserDtos(users);
        }
        return userMapper.toUserDtos(userRepository.findAllByIdIn(ids, PageRequest.of(from / size, size)).getContent());
    }

    public UserDto createUser(NewUserRequest newUserRequest) {
        try {
            User user = userMapper.toUserRequest(newUserRequest);
            userRepository.save(user);
            return userMapper.toUserDto(user);
        } catch (Exception e) {
            throw new ConflictException("Такой email уже используется");
        }
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
