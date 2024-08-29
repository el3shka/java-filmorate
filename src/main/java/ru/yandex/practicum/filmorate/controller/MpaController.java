package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class MpaController {
    private final MpaService service;

    @GetMapping
    private List<Mpa> getAll() {
        return service.getAll();
    }


    @GetMapping("{id}")
    public Mpa getMpaName(@PathVariable long id) {
        return service.getName(id);
    }
}
