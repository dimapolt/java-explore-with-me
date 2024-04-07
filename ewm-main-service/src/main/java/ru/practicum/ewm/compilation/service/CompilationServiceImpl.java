package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.dto.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.storage.CompilationStorage;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.storage.EventStorage;
import ru.practicum.ewm.exception.NoDataFoundException;
import ru.practicum.ewm.util.requests.EwmRequestParams;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.ewm.compilation.dto.mapper.CompilationMapper.toDto;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationStorage storage;
    private final EventStorage eventStorage;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>(eventStorage.findAllById(newCompilationDto.getEvents()));

        Compilation compilation = new Compilation(null, events,
                newCompilationDto.getPinned(), newCompilationDto.getTitle());

        return toDto(storage.save(compilation));
    }

    @Override
    @Transactional
    public String deleteCompilation(Long compId) {
        Compilation compilation = storage.findById(compId).orElseThrow(
                () -> new NoDataFoundException("Такой подборки нет в базе"));

        storage.delete(compilation);

        return "Подборка удалена";
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilation) {
        Compilation compilation = storage.findById(compId).orElseThrow(
                () -> new NoDataFoundException("Такой подборки нет в базе"));

        if (updateCompilation.getEvents() != null) {
            compilation.setEvents(new HashSet<>(eventStorage.findAllById(updateCompilation.getEvents())));
        }
        if (updateCompilation.getPinned() != null) {
            compilation.setPinned(updateCompilation.getPinned());
        }
        if (updateCompilation.getTitle() != null) {
            compilation.setTitle(updateCompilation.getTitle());
        }

        return toDto(storage.save(compilation));
    }

    @Override
    @Transactional
    public List<CompilationDto> getCompilations(Boolean pinned, EwmRequestParams params) {
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = storage.findAllWithPinned(pinned, params.getPageable());
        } else {
            compilations = storage.findAll(params.getPageable()).getContent();
        }

        return compilations.stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = storage.findById(compId).orElseThrow(
                () -> new NoDataFoundException("Подборка не найдена"));

        return toDto(compilation);
    }
}
