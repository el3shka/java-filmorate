package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.*;


@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {

    protected final Map<Long, User> users = new HashMap<>();
    protected Long idUserGen = 1L;


    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User add(@RequestBody User user) { //
        validateUser(user);
        checkUserLogin(user);
        user.setId(idUserGen);
        users.put(idUserGen, user);
        idUserGen++;
        log.info("Пользователь с id = {} успешно добален", user.getId());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        validateUser(user);
        if (users.containsKey(user.getId())) {
            checkUserLogin(user);
            users.put(user.getId(), user);
            log.info("Пользователь с id = {} успешно обновлен", user.getId());
        } else {
            log.info("Пользователь с id {} не обновлен, т.к. не зарегистрирован", user.getId());
            throw new ValidationException("Невозможно обновить данные пользователя. Такого пользователя не существует");
        }
        return user;
    }

    private void checkUserLogin(User user) {
        for (User value : users.values()) {
            if (user.getLogin().equals(value.getLogin())) {
                throw new ValidationException("Пользователь с таким логином зарегистрирован");
            }
        }
    }

    public static void validateUser(User user) {
        String mail = user.getEmail();
        if (mail == null || mail.isBlank()) {
            log.info("Пользователь с id = {} не указал электронную почту", user.getId());
            throw new ValidationException("Не указана электронная почта.");
        } else if (!mail.contains("@")) {
            log.info("Электронная почта для пользователя с id = {} указана некорректно", user.getId());
            throw new ValidationException("Электронная почта указана некорректно.");
        } else if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.info("Пользователь с id = {} не указал логин", user.getId());
            throw new ValidationException("Логин не может быть пустым или содержать пробелы.");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("В качестве имени пользователя с id = {} будет использоваться логин", user.getId());
        } else if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Пользователь с id = {} некорректно указал дату рождения", user.getId());
            throw new ValidationException("Дата рождения введена неккоректно.");
        }
        log.info("Пользователь с id = {} успешно прошел валидацию", user.getId());
    }
}
