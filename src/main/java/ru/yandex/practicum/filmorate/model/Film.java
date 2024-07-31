package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@ToString
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    Set<Integer> likes = new HashSet<>();

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
        if (id == 0) {
            this.id = 1; // Если ID равен 0, устанавливаем его равным 1
        } else {
            this.id = id;
        }
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}