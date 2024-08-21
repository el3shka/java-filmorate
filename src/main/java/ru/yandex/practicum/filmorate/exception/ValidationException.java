package ru.yandex.practicum.filmorate.controller;

public class ValidationException extends RuntimeException {
    String message;

    public ValidationException(String message) {
        super(message);
    }
}