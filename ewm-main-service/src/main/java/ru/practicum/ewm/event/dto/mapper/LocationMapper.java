package ru.practicum.ewm.event.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.event.model.Location;

@UtilityClass
public class LocationMapper {
    public static Location toEntity(LocationDto locationDto) {
        return new Location(null,
                locationDto.getLat(),
                locationDto.getLon());
    }

    public static LocationDto toDto(Location location) {
        return new LocationDto(location.getLat(),
                location.getLon());
    }
}
