package ru.yandex.practicum.filmorate.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validation.annotation.ReleaseDate;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {
    private LocalDate earliestDate;

    @Override
    public void initialize(ReleaseDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);

        try {
            this.earliestDate = LocalDate.parse(constraintAnnotation.earliestDate());
            log.info("Earliest date: {}", earliestDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for earliestDate: " + constraintAnnotation.earliestDate());
        }
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(earliestDate);
    }
}
