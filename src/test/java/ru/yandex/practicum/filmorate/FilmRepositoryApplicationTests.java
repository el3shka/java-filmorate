package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate")
public class FilmRepositoryApplicationTests {
    private final FilmRepository filmRepository;
    Film film;

    @BeforeEach
    public void setUp() {
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setDuration(160);
        film.setReleaseDate(LocalDate.of(2010, 10, 10));
        film.setMpa(new Mpa(1, null));

        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(new Genre(1, null));

        film.setGenres(genres);
    }

    @Test
    public void couldAddFilmTest() {
        assertDoesNotThrow(() -> filmRepository.add(film));
    }

    @Test
    public void couldGetFilmTest() {
        film = filmRepository.add(film);
        Optional<Film> film1 = filmRepository.getFilm(film.getId());

        assertThat(film1.isPresent());
    }

    @Test
    public void couldUpdateFilmTest() {
        film = filmRepository.add(film);
        film.setName("updated film");
        filmRepository.update(film);

        assertThat(filmRepository.getFilm(film.getId()).get().getName()).isEqualTo("updated film");
    }

    @Test
    public void couldGetListOfFilmsTest() {
        Film film1 = new Film();
        film1.setName("film1");
        film1.setDescription("description1");
        film1.setDuration(160);
        film1.setMpa(new Mpa(1, null));
        film1.setReleaseDate(LocalDate.of(2010, 10, 10));
        Film film2 = new Film();
        film2.setName("film2");
        film2.setDescription("description2");
        film2.setDuration(160);
        film2.setMpa(new Mpa(1, null));
        film2.setReleaseDate(LocalDate.of(2010, 10, 10));

        film1 = filmRepository.add(film1);
        film2 = filmRepository.add(film2);

        List<Film> films = (List<Film>) filmRepository.getFilms(Optional.empty());
        assertThat(films.size()).isEqualTo(2);
        assertThat(films.contains(film1)).isTrue();
        assertThat(films.contains(film2)).isTrue();
    }
}
