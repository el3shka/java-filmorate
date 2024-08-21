package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film add(Film film);

    void delete(long id);

    Film update(Film film);

    Optional<Film> getFilm(long id);

    Collection<Film> getFilms(Optional<Integer> limit);

    void setLike(long filmId, long userId);

    void removeLike(long filmId, long userId);
}
