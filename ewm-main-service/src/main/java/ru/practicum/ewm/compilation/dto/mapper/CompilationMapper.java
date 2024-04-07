package ru.practicum.ewm.compilation.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.mapper.EventMapper;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public static CompilationDto toDto(Compilation compilation) {
        Set<EventShortDto> eventsShort = compilation.getEvents().stream()
                .map(EventMapper::toShortDto).collect(Collectors.toSet());

        return new CompilationDto(eventsShort,
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle());
    }
}
