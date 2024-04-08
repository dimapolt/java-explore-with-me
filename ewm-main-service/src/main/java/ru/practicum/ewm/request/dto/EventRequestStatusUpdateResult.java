package ru.practicum.ewm.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<RequestDto> confirmedRequests = new ArrayList<>();
    private List<RequestDto> rejectedRequests = new ArrayList<>();
}
