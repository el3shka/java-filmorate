package ru.yandex.practicum.filmorate.validation.annotation;


import jakarta.validation.Constraint;
import ru.yandex.practicum.filmorate.validation.validator.ReleaseDateValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReleaseDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReleaseDate {
    String earliestDate();

    String message() default "wrong release date";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
