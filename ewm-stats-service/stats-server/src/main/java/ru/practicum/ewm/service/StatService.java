package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.StatDtoIn;
import ru.practicum.ewm.dto.StatDtoOut;
import ru.practicum.ewm.util.StatRequest;

import java.util.List;

public interface StatService {
    StatDtoIn addStat(StatDtoIn statDto);

    List<StatDtoOut> getStats(StatRequest requester);

}
