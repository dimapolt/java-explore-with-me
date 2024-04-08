package ru.practicum.ewm.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.request.model.status.ReqStatus;

import java.util.List;

@Data
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private ReqStatus status;
}
