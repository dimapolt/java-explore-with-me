package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.event.model.status.EvenStateUser;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "Неверная длина поля 'annotation' (верно - >20 и <2000)!")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Неверная длина поля 'description' (верно - >20 и <7000)!")
    private String description;
    @NotNull(message = "Пустое поле 'eventDate'")
    @Future(message = "Неверная дата в поле 'eventDate'!")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EvenStateUser stateAction;
    private String title;
}
