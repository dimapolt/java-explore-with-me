package ru.practicum.ewm.compilation.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class NewCompilationDto {
    private final Set<Long> events;
    private final Boolean pinned;
    @Size(min = 1, max = 50)
    private final String title;
}
