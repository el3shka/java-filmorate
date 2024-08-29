package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreRepository;

    public Genre getGenre(Long id) {
        Optional<Genre> genre = genreRepository.getGenre(id);

        if (genre.isEmpty()) {
            throw new NotFoundException("Genre with id " + id + " not found");
        }
        return genre.get();
    }

    public List<Genre> getGenres() {
        return genreRepository.getGenres()
                .stream()
                .sorted(Comparator.comparing(Genre::getId))
                .toList();
    }
}
