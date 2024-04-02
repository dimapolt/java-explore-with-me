package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.util.EwmValidate;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Неверная длина поля 'annotation' (верно - >20 и <2000)!")
    private String annotation;
    @NotNull(message = "Пустое поле 'category'", groups = {EwmValidate.OnCreate.class})
    private long category;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Неверная длина поля 'description' (верно - >20 и <7000)!")
    private String description;
    @NotNull(message = "Пустое поле 'eventDate'", groups = {EwmValidate.OnCreate.class})
    @Future(message = "Неверная дата в поле 'eventDate'!")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;
    @NotNull(message = "Пустое поле 'location'")
    private LocationDto location;
    private boolean paid = true;
    @PositiveOrZero(message = "Поле 'participantLimit' меньше нуля!")
    private int participantLimit = 0;
    private boolean requestModeration = false;
    @NotBlank
    @Size(min = 3, max = 120, message = "Неверная длина поля 'title' (верно - >3 и <120)!")
    private String title;
}
