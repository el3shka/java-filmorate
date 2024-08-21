package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmStorage;
import ru.yandex.practicum.filmorate.repository.GenreStorage;
import ru.yandex.practicum.filmorate.repository.MpaStorage;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmRepository;
    private final UserStorage userRepository;
    private final GenreStorage genreRepository;
    private final MpaStorage mpaRepository;

    public Film createFilm(Film film) {
        film = filmRepository.add(film);

        log.info("Film created: {}", film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (filmRepository.getFilm(film.getId()).isEmpty()) {
            log.info("Film update failed: {}", film);
            throw new NotFoundException("Film with name " + film.getName() + " does not exist yet");
        }

        film = filmRepository.update(film);

        log.info("Film updated: {}", film);
        return film;
    }

    public void setLike(long filmId, long userId) {
        if (filmRepository.getFilm(filmId).isEmpty()) {
            throw new NotFoundException("Film with id " + filmId + " does not exist yet");
        }
        if (userRepository.get(userId).isEmpty()) {
            throw new NotFoundException("User with id " + userId + " does not exist yet");
        }

        filmRepository.setLike(filmId, userId);
        log.info("Film was liked");
    }

    public void removeLike(long filmId, long userId) {
        if (filmRepository.getFilm(filmId).isEmpty()) {
            throw new NotFoundException("Film with id " + filmId + " does not exist yet");
        }
        if (userRepository.get(userId).isEmpty()) {
            throw new NotFoundException("User with id " + userId + " does not exist yet");
        }

        filmRepository.removeLike(filmId, userId);
        log.info("Like was deleted");
    }

    public Film getFilm(long id) {
        Optional<Film> film = filmRepository.getFilm(id);
        if (film.isEmpty()) {
            throw new NotFoundException("Film with id " + id + " does not exist yet");
        }

        film.get().setMpa(mpaRepository.getName(film.get().getMpa().getId()).get());

        return film.get();
    }

    public List<Film> getFilms() {
        return (List<Film>) filmRepository.getFilms(Optional.empty());
    }

    public List<Film> getPopularFilms(int count) {
        log.info("List of popular films of " + count + " is displayed");
        return (List<Film>) filmRepository.getFilms(Optional.of(count));
    }

    public void deleteFilm(long id) {
        filmRepository.delete(id);
    }

}
