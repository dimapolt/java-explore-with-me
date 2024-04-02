package ru.practicum.ewm.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class StatRequest {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final List<String> uris;
    private final boolean unique;
}
