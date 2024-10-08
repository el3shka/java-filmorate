package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Optional<Genre> getGenre(Long id);

    List<Genre> getGenres();

    List<Genre> getGenresList(List<Long> ids);
}
