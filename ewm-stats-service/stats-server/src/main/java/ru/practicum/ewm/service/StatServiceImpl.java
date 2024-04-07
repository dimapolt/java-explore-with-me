package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.StatDtoIn;
import ru.practicum.ewm.dto.StatDtoOut;
import ru.practicum.ewm.model.Stat;
import ru.practicum.ewm.storage.StatStorage;
import ru.practicum.ewm.util.StatRequest;

import java.util.List;

import static ru.practicum.ewm.mapper.DtoMapper.toDto;
import static ru.practicum.ewm.mapper.DtoMapper.toEntity;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {
    private final StatStorage storage;

    @Override
    public StatDtoIn addStat(StatDtoIn statDto) {
        log.info("Вызов addStat в StatServiceImpl");
        Stat returned = storage.save(toEntity(statDto));
        return toDto(returned);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatDtoOut> getStats(StatRequest request) {
        log.info("Вызов getStats в StatServiceImpl");

        if (request.getStart().isAfter(request.getEnd())) {
            throw new IllegalArgumentException("Начало периода после окончания");
        }

        if (request.getUris().isEmpty()) {
            if (request.isUnique()) {
                return storage.findAllAndUnique(request.getStart(), request.getEnd());
            } else {
                return storage.findAllAndNotUnique(request.getStart(), request.getEnd());
            }
        } else {
            if (request.isUnique()) {
                return storage.findByUrisAndUnique(request.getStart(), request.getEnd(), request.getUris());
            } else {
                return storage.findByUrisAndNotUnique(request.getStart(), request.getEnd(), request.getUris());
            }
        }
    }

}
