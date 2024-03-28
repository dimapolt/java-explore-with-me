package ru.practicum.ewm.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.StatDtoIn;
import ru.practicum.ewm.dto.StatDtoOut;
import ru.practicum.ewm.model.Stat;

@UtilityClass
public class DtoMapper {

    public static StatDtoOut toDto(Stat stat, Long hits) {
        return new StatDtoOut(stat.getApp(),
                stat.getUri(),
                hits);
    }

    public static StatDtoIn toDto(Stat stat) {
        return new StatDtoIn(stat.getApp(),
                stat.getUri(),
                stat.getIp(),
                stat.getTimestamp());
    }

    public static Stat toEntity(StatDtoIn statDtoIn) {
        return new Stat(null,
                statDtoIn.getApp(),
                statDtoIn.getUri(),
                statDtoIn.getIp(),
                statDtoIn.getTimestamp());
    }

}
