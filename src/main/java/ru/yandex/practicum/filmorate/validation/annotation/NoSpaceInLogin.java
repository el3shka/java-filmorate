package ru.yandex.practicum.filmorate.validation.annotation;

import jakarta.validation.Constraint;
import ru.yandex.practicum.filmorate.validation.validator.SpaceInLoginValidation;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SpaceInLoginValidation.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSpaceInLogin {
    String message() default "you have space in login";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
