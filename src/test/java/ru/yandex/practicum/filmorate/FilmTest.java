package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {
    protected Film filmTestObject;

    @BeforeEach
    public void prepareData() {
        filmTestObject = Film.builder()
                .id(1L)
                .name("бойцовский клуб")
                .description("Сотрудник страховой компании страдает хронической бессонницей и отчаянно пытается вырваться из мучительно скучной жизни.")
                .releaseDate(LocalDate.of(2011, 1, 13))
                .duration(145)
                .build();
    }

    @Test
    public void nameValidateTest() {
        filmTestObject.setName("");
        Exception exceptionNullName = assertThrows(ValidationException.class, () -> FilmController.validateFilm(filmTestObject));
        assertEquals("Название фильма не может быть пустым или состоять из пробелов", exceptionNullName.getMessage());

        filmTestObject.setName("");
        Exception exceptionEmptyName = assertThrows(ValidationException.class, () -> FilmController.validateFilm(filmTestObject));
        assertEquals("Название фильма не может быть пустым или состоять из пробелов", exceptionEmptyName.getMessage());

        filmTestObject.setName("   ");
        Exception exceptionNameFromSpaces = assertThrows(ValidationException.class, () -> FilmController.validateFilm(filmTestObject));
        assertEquals("Название фильма не может быть пустым или состоять из пробелов", exceptionNameFromSpaces.getMessage());
    }

    @Test
    public void descriptionValidateTest() {
        filmTestObject.setDescription("Терзаемый хронической бессонницей и отчаянно пытающийся вырваться из мучительно скучной жизни клерк встречает некоего Тайлера Дардена, харизматического торговца мылом с извращенной философией. Тайлер уверен, что самосовершенствование — удел слабых, а саморазрушение — единственное, ради чего стоит жить.");
        Exception exceptionTooLongDescription = assertThrows(ValidationException.class, () -> FilmController.validateFilm(filmTestObject));
        assertEquals("Длина описания фильма не должна превышать 200 символов.",
                exceptionTooLongDescription.getMessage());
    }
    
    @Test
    public void durationValidateTest() {
        filmTestObject.setDuration(-120);
        Exception exceptionNegativeDuration = assertThrows(ValidationException.class, () -> FilmController.validateFilm(filmTestObject));
        assertEquals("Продолжительность фильма не может быть отрицательной.",
                exceptionNegativeDuration.getMessage());
    }

    @Test
    public void durationReleaseDateTest() {
        filmTestObject.setReleaseDate(LocalDate.of(1895, 12, 27));
        Exception exceptionReleaseDate = assertThrows(ValidationException.class, () -> FilmController.validateFilm(filmTestObject));
        assertEquals("Дата релиза некорректная.",
                exceptionReleaseDate.getMessage());
    }

    @Test
    public void successfulValidateFilmTest() {
        assertDoesNotThrow(() -> FilmController.validateFilm(filmTestObject));
    }

}
