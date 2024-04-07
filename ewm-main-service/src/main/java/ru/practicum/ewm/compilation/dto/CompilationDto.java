package ru.practicum.ewm.compilation.dto;

import lombok.Data;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.Set;

@Data
public class CompilationDto {
    private final Set<EventShortDto> events;
    private final Long id;
    private final Boolean pinned;
    private final String title;
}
