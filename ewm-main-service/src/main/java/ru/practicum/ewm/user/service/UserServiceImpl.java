package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.AlreadyExistException;
import ru.practicum.ewm.exception.NoDataFoundException;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserDtoCreate;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.storage.UserStorage;
import ru.practicum.ewm.util.EwmRequest;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.user.dto.UserMapper.toDto;
import static ru.practicum.ewm.user.dto.UserMapper.toEntity;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage storage;

    @Override
    public UserDto createUser(UserDtoCreate userDto) {
        User user = toEntity(userDto);
        try {
            return toDto(storage.save(user));
        } catch (RuntimeException exception) {
            throw new AlreadyExistException(String.format("Адрес электронной почты '%s' занят!", user.getEmail()));
        }
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, EwmRequest params) {
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize());
        List<User> users;

        if (ids.isEmpty()) {
            users = storage.findAll(pageable).toList();
        } else {
            users = storage.findAllByIdIn(ids, pageable);
        }

        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public String deleteUser(long id) {
        User user = storage.findById(id).orElseThrow(() -> new NoDataFoundException("User not found"));
        storage.delete(user);
        return "Пользователь удален";
    }
}
