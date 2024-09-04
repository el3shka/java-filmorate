package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreRepository implements GenreStorage {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String SELECT_GENRE_BY_ID = "SELECT * FROM genres WHERE id = :id";
    private static final String SELECT_ALL_GENRES = "SELECT * FROM genres";

    @Override
    public Optional<Genre> getGenre(Long id) {
        try {
            Genre genre = jdbcTemplate.queryForObject(SELECT_GENRE_BY_ID,
                    new MapSqlParameterSource("id", id),
                    new GenreRowMapper());
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getGenres() {
        return jdbcTemplate.query(SELECT_ALL_GENRES, new GenreRowMapper());
    }

    @Override
    public List<Genre> getGenresList(List<Long> ids) {
        String sql = "SELECT * FROM genres WHERE id IN (:id);";
        return jdbcTemplate.query(sql, Map.of("id", ids), genreRowMapper());
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name"));
    }
}
