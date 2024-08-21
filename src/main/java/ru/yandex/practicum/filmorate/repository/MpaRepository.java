package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaRepository implements MpaStorage {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String GET_NAME_QUERY = "SELECT * FROM mpa WHERE id = :id";
    private static final String GET_ALL_QUERY = "SELECT * FROM mpa";

    @Override
    public Optional<Mpa> getName(long id) {
        try {
            Mpa mpa = jdbcTemplate.queryForObject(
                    GET_NAME_QUERY,
                    new MapSqlParameterSource("id", id),
                    new MpaRowMapper());

            return Optional.ofNullable(mpa);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, new MpaRowMapper());
    }
}
