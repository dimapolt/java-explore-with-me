package ru.practicum.ewm.util;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Getter
public class EwmRequest {
    private Pageable pageable;

    public EwmRequest(int from, int size) {
        this.pageable = PageRequest.of(from / size, size);
    }

}
