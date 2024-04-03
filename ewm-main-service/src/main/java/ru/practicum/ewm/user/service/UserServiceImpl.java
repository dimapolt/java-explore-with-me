package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.AlreadyExistException;
import ru.practicum.ewm.exception.NoDataFoundException;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.storage.UserStorage;
import ru.practicum.ewm.util.EwmRequest;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.user.dto.mapper.UserMapper.toDto;
import static ru.practicum.ewm.user.dto.mapper.UserMapper.toEntity;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage storage;

    @Override
    @Transactional
    public UserDto createUser(NewUserRequest userDto) {
        User user = toEntity(userDto);
        try {
            return toDto(storage.save(user));
        } catch (RuntimeException exception) {
            throw new AlreadyExistException(String.format("Адрес электронной почты '%s' занят!", user.getEmail()));
        }
    }

    @Override
    @Transactional
    public List<UserDto> getUsers(List<Long> ids, EwmRequest params) {
        Pageable pageable = params.getPageable();
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
    @Transactional
    public String deleteUser(long id) {
        User user = storage.findById(id).orElseThrow(() -> new NoDataFoundException("User not found"));
        storage.delete(user);
        return "Пользователь удален";
    }
}
