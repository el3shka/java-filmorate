package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorUserTest {
    protected User userTestObject;

    @BeforeEach
    public void prepareData() {
        userTestObject = User.builder()
                .id(1L)
                .email("harv3st3r666@gmail.com")
                .login("el3shka")
                .birthday(LocalDate.of(1994, 7, 4))
                .name(" ")
                .build();
    }


    @Test
    public void loginValidateTest() {
        userTestObject.setLogin("Логин с пробелом");
        Exception exceptionLoginWithSpace = assertThrows(ValidationException.class,
                () -> ValidatorUser.validateUser(userTestObject));
        assertEquals("Поле логин не может быть пустым или содержать пробелы", exceptionLoginWithSpace.getMessage());
    }

    @Test
    public void nameValidateTest() {
        userTestObject.setName(null);
        ValidatorUser.validateUser(userTestObject);
        assertEquals(userTestObject.getLogin(), userTestObject.getName());

        userTestObject.setName("");
        ValidatorUser.validateUser(userTestObject);
        assertEquals(userTestObject.getLogin(), userTestObject.getName());

        userTestObject.setName(" ");
        ValidatorUser.validateUser(userTestObject);
        assertEquals(userTestObject.getLogin(), userTestObject.getName());
    }
}