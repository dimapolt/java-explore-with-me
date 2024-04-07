package ru.practicum.ewm.compilation.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateCompilationRequest {
    private final Set<Long> events;
    private final Boolean pinned;
    private final String title;
}
