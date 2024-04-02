package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserDtoCreate;
import ru.practicum.ewm.util.EwmRequest;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDtoCreate user);

    List<UserDto> getUsers(List<Long> ids, EwmRequest params);

    String deleteUser(long id);
}
