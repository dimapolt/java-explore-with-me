package ru.practicum.ewm.rating.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.user.dto.UserDto;

@Data
@AllArgsConstructor
public class UserRatingOut {
    private UserDto user;
    private Float rating;
}
