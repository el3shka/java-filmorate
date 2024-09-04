package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserControllerTests {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private User user;
    private Set<ConstraintViolation<User>> violations;

    @Autowired
    private UserController userController;


    @BeforeEach
    public void setUp() {
        user = new User();
        user.setLogin("user");
        user.setEmail("user@yandex.ru");
        user.setName("user");
        user.setBirthday(LocalDate.of(2001, 1, 1));
    }

    @Test
    public void shouldAddUser() {
        this.violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldNotAddUserWithoutSymbolAt() {
        user.setEmail("user.ru");

        this.violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotAddUserWithEmptyEmail() {
        user.setEmail("");

        this.violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotAddUserWithSpaceInLogin() {
        user.setLogin("user u");

        this.violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotAddUserWithEmptyLogin() {
        user.setLogin("");

        this.violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotAddUserWithBirthdayInFuture() {
        user.setBirthday(LocalDate.of(3025, 1, 1));

        this.violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }


}
