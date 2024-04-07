package ru.practicum.ewm.util.requests;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Getter
public class EwmRequestParams {
    private final Pageable pageable;

    public EwmRequestParams(int from, int size) {
        this.pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());
    }

    public EwmRequestParams(int from, int size, String sort) {
        this.pageable = PageRequest.of(from / size, size, Sort.by(sort).descending());
    }
}
