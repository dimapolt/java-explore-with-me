package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.status.EventState;
import ru.practicum.ewm.user.dto.UserShortDto;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFullDto {
    private Long id;

    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    @Size(min = 3, max = 120, message = "Неверная длина поля 'title' (верно - >3 и <120)!")
    private String title;
    private Long views;
}
