package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRepository mpaRepository;

    public Mpa getName(long id) {
        Optional<Mpa> mpa = mpaRepository.getName(id);

        if (mpa.isEmpty()) {
            throw new NotFoundException("Mpa with id " + id + " not found");
        }
        return mpa.get();
    }

    public List<Mpa> getAll() {
        return mpaRepository.getAll()
                .stream()
                .sorted(Comparator.comparingDouble(Mpa::getId))
                .toList();
    }
}
