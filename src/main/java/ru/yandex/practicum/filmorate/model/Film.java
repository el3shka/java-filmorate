package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.annotation.ReleaseDate;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@NoArgsConstructor
public class Film {
    private long id;

    @NotNull
    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    private String description;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ReleaseDate(earliestDate = "1895-12-28")
    @NotNull
    private LocalDate releaseDate;

    @Min(1)
    private int duration;

    private Set<Long> likes = new LinkedHashSet<>();

    @NotNull
    @Valid
    private Mpa mpa;

    @Valid
    Set<Genre> genres = new LinkedHashSet<>();
}
