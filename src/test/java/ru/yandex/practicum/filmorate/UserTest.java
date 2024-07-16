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
                .email("harv3st3r@gmail.com")
                .login("el3shque")
                .birthday(LocalDate.of(1994, 7, 4))
                .name("Харвэстэр")
                .build();
    }

    @Test
    public void emailValidateTest() {
        userTestObject.setEmail(null);
        Exception exceptionNullEmail = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Не указана электронная почта", exceptionNullEmail.getMessage());


        userTestObject.setEmail("");
        Exception exceptionEmptyEmail = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Не указана электронная почта", exceptionEmptyEmail.getMessage());


        userTestObject.setEmail("   ");
        Exception exceptionEmailFromSpaces = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Не указана электронная почта", exceptionEmailFromSpaces.getMessage());

        userTestObject.setEmail("harv3st3rgmail.com");
        Exception exceptionEmailWithoutDog = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Электронная почта указана неверно", exceptionEmailWithoutDog.getMessage());
    }

    @Test
    public void loginValidateTest() {
        userTestObject.setLogin(null);
        Exception exceptionNullLogin = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Поле логин не может быть пустым или содержать пробелы", exceptionNullLogin.getMessage());

        userTestObject.setLogin("");
        Exception exceptionEmptyLogin = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Поле логин не может быть пустым или содержать пробелы", exceptionEmptyLogin.getMessage());

        userTestObject.setLogin("Логин содержит пробел.");
        Exception exceptionLoginWithSpace = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("Поле логин не может быть пустым или содержать пробелы", exceptionLoginWithSpace.getMessage());
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
        userTestObject.setBirthday(LocalDate.of(2001,2,4));
        Exception exceptionBirthdayInFuture = assertThrows(ValidationException.class, () -> UserController.validateUser(userTestObject));
        assertEquals("INFO: Неккоректно введена дата рождения", exceptionBirthdayInFuture.getMessage());
    }

    @Test
    public void successfulValidateUserTest() {
        assertDoesNotThrow(() -> UserController.validateUser(userTestObject));
    }
}