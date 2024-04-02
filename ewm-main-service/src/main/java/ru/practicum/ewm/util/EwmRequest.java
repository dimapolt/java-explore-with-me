package ru.practicum.ewm.util;

import lombok.Getter;

@Getter
public class EwmRequest {
    private int page;
    private int size;

    public EwmRequest(int from, int size) {
        this.page = from / size;
        this.size = size;
    }
}
