package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.util.EwmValidate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoCreate {
    @NotBlank(message = "Имя не может быть пустым!", groups = {EwmValidate.OnCreate.class})
    @Size(min = 2, max = 250, message = "Неверная длина имени!")
    private String name;
    @Email(message = "Неверный адрес электронной почты!", groups = {EwmValidate.OnCreate.class})
    @NotBlank(message = "Адрес электронной почты не может быть пустым!", groups = {EwmValidate.OnCreate.class})
    @Size(min = 6, max = 254, message = "Неверная длина email!")
    private String email;
}
