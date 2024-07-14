package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    
    private static final LocalDate THE_FIRST_FILM_REALISE_DATE = LocalDate.of(1895, 12, 28);
    protected final Map<Long, Film> films = new HashMap<>();
    protected Long idFilmGen = 1L;

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film add(@RequestBody Film film) {
        validateFilm(film);
        film.setId(idFilmGen);
        films.put(film.getId(), film);
        idFilmGen += 1;
        log.info("Фильм с id = {} успешно добален", film.getId());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        validateFilm(film);
        Long idFilm = film.getId();
        if (films.containsKey(idFilm)) {
            films.put(idFilm, film);
            log.warn("Фильм с id = {} добавлен",
                    film.getId());
        } else {
            throw new ValidationException("Невозможно обновить фильм");
        }
        return film;
    }

    public static void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("В фильме с id = {} необходимо название", film.getId());
            throw new ValidationException("Название фильма не может быть пустым или состоять из пробелов");
        } else if (film.getDescription() == null) {
            log.warn("В фильме с id = {} превышение длины описания", film.getId());
            throw new ValidationException("Длина описания фильма не должна превышать 200 символов.");
        } else if (film.getDescription().length() > 200) {
            log.warn("В фильме с id = {} превышение длины описания", film.getId());
            throw new ValidationException("Длина описания фильма не должна превышать 200 символов.");
        } else {
            if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(THE_FIRST_FILM_REALISE_DATE)) {
                log.warn("В фильме с id = {} дата релиза некорректна", film.getId());
                throw new ValidationException("Дата релиза некорректная.");
            } else if (film.getDuration() <= 0) {
                log.warn("В фильме с id = {} продолжительность указана некорректно", film.getId());
                throw new ValidationException("Продолжительность фильма не может быть отрицательной.");
            }
        }
        log.info("Валидация фильма с id = {} прошла успешно", film.getId());
    }
}
