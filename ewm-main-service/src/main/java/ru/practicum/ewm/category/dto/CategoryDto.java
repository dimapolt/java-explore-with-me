package ru.practicum.ewm.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private long id;
    @NotBlank
    @Size(min = 1, max = 50, message = "Неверная длина поля 'name' (верно - >1 и <50)!")
    private String name;
}
