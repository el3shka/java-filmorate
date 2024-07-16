package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    protected User userTestObject;

    @BeforeEach
    public void prepareData() {
        userTestObject = User.builder()
                .id(1L)
                .email("el3shka@gmail.com")
                .login("el3shka")
                .birthday(LocalDate.of(1994, 7, 4))
                .name("pwnztriplesix")
                .build();
    }

    @Test
    public void emailValidateTest() {
        userTestObject.setEmail(null);
        Exception exceptionNullEmail = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Не указана электронная почта.", exceptionNullEmail.getMessage());


        userTestObject.setEmail("");
        Exception exceptionEmptyEmail = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Не указана электронная почта.", exceptionEmptyEmail.getMessage());


        userTestObject.setEmail("   ");
        Exception exceptionEmailFromSpaces = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Не указана электронная почта.", exceptionEmailFromSpaces.getMessage());

        userTestObject.setEmail("el3shkagmail.com");
        Exception exceptionEmailWithoutDog = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Электронная почта указана некорректно.", exceptionEmailWithoutDog.getMessage());
    }

    @Test
    public void loginValidateTest() {
        userTestObject.setLogin(null);
        Exception exceptionNullLogin = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Логин не может быть пустым или содержать пробелы.", exceptionNullLogin.getMessage());

        userTestObject.setLogin("");
        Exception exceptionEmptyLogin = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Логин не может быть пустым или содержать пробелы.", exceptionEmptyLogin.getMessage());

        userTestObject.setLogin("В логине присутствует пробел.");
        Exception exceptionLoginWithSpace = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Логин не может быть пустым или содержать пробелы.", exceptionLoginWithSpace.getMessage());
    }

    @Test
    public void nameValidateTest() {
        userTestObject.setName(null);
        UserController.validateUser(userTestObject);
        assertEquals(userTestObject.getLogin(), userTestObject.getName());

        userTestObject.setName("");
        UserController.validateUser(userTestObject);
        assertEquals(userTestObject.getLogin(), userTestObject.getName());
    }

    @Test
    public void birthdayValidateTest() {
        userTestObject.setBirthday(LocalDate.of(2994,7,4));
        Exception exceptionBirthdayInFuture = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Дата рождения введена неккоректно.", exceptionBirthdayInFuture.getMessage());
    }

    @Test
    public void successfulValidateUserTest() {
        assertDoesNotThrow(() -> UserController.validateUser(userTestObject));
    }
}
