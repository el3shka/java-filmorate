package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {
	FilmController filmController;
	UserController userController;
	FilmService filmService;
	UserService userService;
	UserStorage userStorage;
	FilmStorage filmStorage;

	@BeforeEach
	void createControllers() {
		userStorage = new InMemoryUserStorage();
		filmStorage = new InMemoryFilmStorage();
		filmService = new FilmService(filmStorage, userStorage);
		userService = new UserService(userStorage);
		filmController = new FilmController(filmService);
		userController = new UserController(userService);
	}

	@Test
	void wrongRealiseData() {
		final Film film = new Film(1, "Бойцовский клуб", "Сотрудник страховой компании страдает хронической бессонницей и отчаянно пытается вырваться из мучительно скучной жизни.",
				LocalDate.of(1800, 1, 1), 100);
		assertThrows(ValidationException.class, () ->
						filmController.create(film),
				"Некорректная дата релиза");
	}

	@Test
	void wrongDescription() {
		final Film film = new Film(1, "Интересный фильм", "Сотрудник страховой компании страдает хронической бессонницей и отчаянно пытается вырваться из мучительно скучной жизни. Однажды в очередной командировке он встречает некоего Тайлера Дёрдена — харизматического торговца мылом с извращенной философией. Тайлер уверен, что самосовершенствование — удел слабых, а единственное, ради чего стоит жить, — саморазрушение.",
				LocalDate.of(2011, 1, 13), 139);
		assertThrows(ValidationException.class, () ->
						filmController.create(film),
				"Некорректное описание");
	}

	@Test
	void emptyName() {
		final Film film = new Film(1, "", "Описание фильма",
				LocalDate.of(2016, 1, 1), 100);
		assertThrows(ValidationException.class, () ->
						filmController.create(film),
				"Отсутствует наименование");
	}

	@Test
	void wrongDuration() {
		final Film film = new Film(1, "", "Описание фильма",
				LocalDate.of(2016, 1, 1), -200);
		assertThrows(ValidationException.class, () ->
						filmController.create(film),
				"Некорректная продолжительность");
	}

	@Test
	void testUserIdAssignment() {
		User user = new User(0, "test@example.com", "login", "Name", LocalDate.of(1990, 1, 1));
		assertEquals(1, user.getId(), "ID должен быть равен 1, если был передан 0");
	}

	@Test
	void wrongEmail() {
		User user = new User(0,"pochtaya.ru", "login", "Виталя",
				LocalDate.of(1993, 5, 12));
		assertThrows(ValidationException.class, () ->
						userController.create(user),
				"Некорректный email");
	}

	@Test
	void emptyEmail() {
		User user = new User(0, null, "login", "Виталя",
				LocalDate.of(1990, 10, 8));
		assertThrows(ValidationException.class, () ->
						userController.create(user),
				"Пустой email");
	}

	@Test
	void emptyLogin() {
		User user = new User(0, "123@ya.ru", "", "Виталя",
				LocalDate.of(1990, 10, 8));
		assertThrows(ValidationException.class, () ->
						userController.create(user),
				"Пустой логин");
	}

	@Test
	void wrongBirthday() {
		User user = new User(0, "123@ya.ru", "login", "Виталя",
				LocalDate.of(2120, 10, 8));
		assertThrows(ValidationException.class, () ->
						userController.create(user),
				"Некорректная дата рождения");
	}
}