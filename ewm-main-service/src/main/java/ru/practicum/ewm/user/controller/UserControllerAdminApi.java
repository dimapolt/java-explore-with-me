package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;
import ru.practicum.ewm.util.EwmValidate;
import ru.practicum.ewm.util.requests.EwmRequestParams;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/users")
@Validated
public class UserControllerAdminApi {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated({EwmValidate.OnCreate.class})
    public UserDto createUser(@RequestBody @Valid NewUserRequest user) {
        log.info("Запрос на создание пользователя");
        return service.createUser(user);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(defaultValue = "") List<Long> ids,
                                  @RequestParam(defaultValue = "0")
                                  @PositiveOrZero(message = "Отрицательное значение параметра 'from'") int from,
                                  @RequestParam(defaultValue = "10")
                                  @Positive(message = "Значение параметра 'size' - " +
                                          "0 или отрицательное") int size) {
        log.info("Запрос на получение пользователей по списку id, с {} по {} элементов", from, size);
        return service.getUsers(ids, new EwmRequestParams(from, size));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteUser(@PathVariable
                             @Positive(message = "Значение переменной 'id' 0 или отрицательное") long userId) {
        log.info("Запрос на удаление пользователя с id=" + userId);
        return service.deleteUser(userId);
    }
}
