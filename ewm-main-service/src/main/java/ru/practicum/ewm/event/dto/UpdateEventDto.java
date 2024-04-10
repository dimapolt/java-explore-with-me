package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventDto {
    @Size(min = 20, max = 2000, message = "Неверная длина поля 'annotation' (верно - >20 и <2000)!")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Неверная длина поля 'description' (верно - >20 и <7000)!")
    private String description;
    @Future(message = "Неверная дата в поле 'eventDate'!")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    @Positive
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(min = 3, max = 120, message = "Неверная длина поля 'title' (верно - >3 и <120)!")
    private String title;
}
