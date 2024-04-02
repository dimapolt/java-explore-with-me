package ru.practicum.ewm.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.user.model.User;

@UtilityClass
public class UserMapper {

    public static UserDto toDto(User user) {
        return new UserDto(user.getName(),
                           user.getId(),
                           user.getEmail());
    }

    public static User toEntity(UserDtoCreate userDto) {
        return new User(null,
                        userDto.getName(),
                        userDto.getEmail());
    }
}
