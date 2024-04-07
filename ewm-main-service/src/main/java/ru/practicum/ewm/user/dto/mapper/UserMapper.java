package ru.practicum.ewm.user.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.model.User;

@UtilityClass
public class UserMapper {

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static User toEntity(NewUserRequest newUserRequest) {
        return new User(null,
                newUserRequest.getName(),
                newUserRequest.getEmail());
    }

    public static UserShortDto toShortDto(User user) {
        return new UserShortDto(user.getId(),
                user.getName());
    }
}
