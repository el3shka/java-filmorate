package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Genre {
    @NotNull
    @Max(10)
    @Min(1)
    private long id;
    private String name;
}
