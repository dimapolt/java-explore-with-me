package ru.practicum.ewm.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.storage.CategoryStorage;
import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.event.dto.mapper.LocationMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.storage.EventStorage;
import ru.practicum.ewm.event.storage.LocationStorage;
import ru.practicum.ewm.exception.NoDataFoundException;
import ru.practicum.ewm.exception.WrongDataException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Сервис осуществляет валидацию входящих данных,
 * а также на предмет наличия в БД, на основе полей с id.
 * В случае успешной проверки добавляет объекты в поля сущности
 */

@Service
@RequiredArgsConstructor
public class EwmValidationService {
    private final UserStorage userStorage;
    private final CategoryStorage categoryStorage;
    private final LocationStorage locationStorage;
    private final EventStorage eventStorage;

    public void formEvent(Event event) {
        Long userId = event.getInitiator().getId();
        Long categoryId = event.getCategory().getId();
        Location location = event.getLocation();
        LocalDateTime eventDate = event.getEventDate();

        checkDate(eventDate, 2);

        User user = checkUserExists(userId);
        event.setInitiator(user);

        Category category = checkCategoryExists(categoryId);
        event.setCategory(category);

        searchOrCreate(location);
    }

    public User checkUserExists(Long id) {
        return userStorage.findById(id).orElseThrow(() -> new NoDataFoundException("Пользователь не найден"));
    }

    public Event checkEventExists(Long id) {
        return eventStorage.findById(id).orElseThrow(() -> new NoDataFoundException("Событие не найдено"));
    }

    public Category checkCategoryExists(Long id) {
        return categoryStorage.findById(id).orElseThrow(() -> new NoDataFoundException("Категория не найдена"));
    }

    public Location searchOrCreate(Location location) {
        Optional<Location> returned = locationStorage.findByLatAndLon(location.getLat(), location.getLon());
        if (returned.isEmpty()) {
            return locationStorage.save(location);
        } else {
            return location;
        }
    }

    public void checkAndSetLocation(LocationDto locationDto, Event event) {
        Optional<Location> locationO = locationStorage.findByLatAndLon(locationDto.getLat(), locationDto.getLon());

        if (locationO.isPresent()) {
            event.setLocation(searchOrCreate(locationO.get()));
        } else {
            event.setLocation(locationStorage.save(LocationMapper.toEntity(locationDto)));
        }
    }

    public void checkDate(LocalDateTime date, int hours) {
        // Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
        if (date.minusHours(hours).isBefore(LocalDateTime.now())) {
            throw new WrongDataException("До события осталось меньше " + hours + " часов, " + date);
        }
    }

    public void checkStartEnd(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Начало периода после окончания");
        }
    }
}
