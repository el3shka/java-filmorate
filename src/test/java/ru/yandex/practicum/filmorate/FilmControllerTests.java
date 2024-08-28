package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTests {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Film film;
    private Set<ConstraintViolation<Film>> violations;

    @Autowired
    FilmController filmController;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(50);
    }

    @Test
    public void shouldNotAddFilmWithEmptyName() {
        film.setName(null);

        this.violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }


    @Test
    public void shouldNotAddFilmWithMoreThen200SymbolsDescription() {
        String description = "Spring Type Conversion\n" +
                "The core.convert package provides a general type conversion system. " +
                "The system defines an SPI to implement type conversion logic and an " +
                "API to perform type conversions at runtime";
        film.setDescription(description);

        this.violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotAddFilmWithTooEarliestDate() {
        LocalDate date = LocalDate.of(1895, 12, 27);
        film.setReleaseDate(date);

        this.violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotAddFilmWithNegativeDuration() {
        film.setDuration(-1);

        this.violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotAddEmptyFilm() {
        Film f1 = new Film();

        assertThrows(ValidationException.class, () -> this.violations = validator.validate(f1));
    }

    @Test
    public void shouldNotUpdateNonExistentFilm() {
        assertThrows(NotFoundException.class, () -> filmController.updateFilm(film));
    }
}
