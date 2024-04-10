package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс инкапсулирует информацию об идентификаторе события (Event)
 * и количестве подтверждённых для него запросов (заявок)
 * Необходим для оптимизации запросов к БД
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCount {
    private long eventId;
    private long count;
}
