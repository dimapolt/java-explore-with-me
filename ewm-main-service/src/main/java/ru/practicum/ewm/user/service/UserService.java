package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.util.requests.EwmRequestParams;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserRequest user);

    List<UserDto> getUsers(List<Long> ids, EwmRequestParams params);

    String deleteUser(long id);
}
