package ru.practicum.ewm.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.storage.EventStorage;
import ru.practicum.ewm.exception.NoDataFoundException;
import ru.practicum.ewm.exception.WrongDataException;
import ru.practicum.ewm.rating.dto.EventRating;
import ru.practicum.ewm.rating.dto.EventRatingOut;
import ru.practicum.ewm.rating.dto.UserRating;
import ru.practicum.ewm.rating.dto.UserRatingOut;
import ru.practicum.ewm.rating.model.Rating;
import ru.practicum.ewm.rating.storage.RatingStorage;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.storage.UserStorage;
import ru.practicum.ewm.util.EwmValidationService;
import ru.practicum.ewm.util.requests.EwmRequestParams;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingStorage storage;
    private final EventStorage eventStorage;
    private final UserStorage userStorage;
    private final EwmValidationService validation;

    @Override
    @Transactional
    public Rating addMark(Long userId, Long eventId, Boolean mark) {
        User rater = validation.checkUserExists(userId);
        Event event = validation.checkEventExists(eventId);

        if (event.getInitiator().getId().equals(userId)) {
            throw new WrongDataException("Пользователь не может ставить оценку своему событию");
        }
        if (event.getEventDate().isAfter(LocalDateTime.now())) {
            throw new WrongDataException("Нельзя оценить событие, которое ещё не состоялось");
        }
        // Проверка, что пользователь делал запрос, и следовательно участвовал в событии
        validation.checkRequest(userId, eventId);
        storage.findByRaterIdAndEventId(userId, eventId).orElseThrow(
                () -> new WrongDataException("Оценка от пользователя с id = " + userId +
                        " событию с id = " + eventId +
                        " уже поставлена"));

        Rating rating = new Rating(null, event, event.getInitiator(), rater, mark);
        return storage.save(rating);
    }

    @Override
    @Transactional
    public Rating changeMark(Long userId, Long eventId) {
        Rating rating = storage.findByRaterIdAndEventId(userId, eventId).orElseThrow(
                () -> new NoDataFoundException("Оценка не найдена"));

        boolean mark = rating.getRate();
        rating.setRate(!mark);
        return rating;
    }

    @Override
    @Transactional
    public String deleteMark(Long userId, Long eventId) {
        Rating rating = storage.findByRaterIdAndEventId(userId, eventId).orElseThrow(
                () -> new NoDataFoundException("Оценка не найдена"));
        storage.delete(rating);
        return "Оценка удалена";
    }

    @Override
    @Transactional
    public List<EventRatingOut> getEventsWithRating(EwmRequestParams params) {
        Map<Long, Float> eventsIdRating = storage.getRatingEvents(params.getPageable()).stream()
                .collect(Collectors.toMap(EventRating::getEvent, EventRating::getRating));

        List<Event> events = eventStorage.findAllById(eventsIdRating.keySet());

        return events.stream()
                .map(event -> {
                    long id = event.getId();
                    EventShortDto eventDto = EventMapper.toShortDto(event);
                    return new EventRatingOut(eventDto, eventsIdRating.get(id));
                }).sorted(Comparator.comparingDouble(EventRatingOut::getRating).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<UserRatingOut> getUsersWithRating(EwmRequestParams params) {
        Map<Long, Float> usersIdRating = storage.getRatingUsers(params.getPageable()).stream()
                .collect(Collectors.toMap(UserRating::getInitiator, UserRating::getRating));

        List<User> users = userStorage.findAllById(usersIdRating.keySet());

        return users.stream()
                .map(user -> {
                    long id = user.getId();
                    UserDto userDto = UserMapper.toDto(user);
                    return new UserRatingOut(userDto, usersIdRating.get(id));
                }).sorted(Comparator.comparingDouble(UserRatingOut::getRating).reversed())
                .collect(Collectors.toList());
    }
}
