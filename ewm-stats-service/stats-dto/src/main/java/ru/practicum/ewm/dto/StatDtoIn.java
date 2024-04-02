package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatDtoIn {
    @NotNull(groups = StatValidate.OnCreate.class)
    @NotBlank(message = "Пустое поле 'app'", groups = StatValidate.OnCreate.class)
    private String app;
    @NotNull(groups = StatValidate.OnCreate.class)
    @NotBlank(message = "Пустое поле 'uri'", groups = StatValidate.OnCreate.class)
    private String uri;
    @NotNull(groups = StatValidate.OnCreate.class)
    @NotBlank(message = "Пустое поле 'ip'", groups = StatValidate.OnCreate.class)
    private String ip;
    @NotNull(message = "Пустое поле 'timestamp'", groups = StatValidate.OnCreate.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime timestamp;
}
