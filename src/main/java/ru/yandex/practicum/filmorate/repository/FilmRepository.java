package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Primary
@RequiredArgsConstructor
@Repository("filmRepository")
public class FilmRepository implements FilmStorage {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String INSERT_FILM_QUERY =
            "INSERT INTO films (film_name, description, release_date, duration, mpa_id) " +
                    "VALUES (:film_name, :description, :release_date, :duration, :mpa_id)";
    private static final String INSERT_GENRE_QUERY =
            "INSERT INTO films_genres (film_id, genre_id) VALUES (:film_id, :genre_id)";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO likes (film_id, user_id) VALUES (:film_id, :user_id)";
    private static final String UPDATE_FILM_QUERY =
            "UPDATE films SET film_name = :film_name, description = :description, release_date = :release_date, " +
                    "duration = :duration, mpa_id = :mpa_id WHERE id = :id";
    private static final String GET_POPULAR_FILMS_QUERY = "SELECT films.* FROM films LEFT JOIN likes " +
            "ON films.id = likes.film_id GROUP BY films.id ORDER BY COUNT(likes.film_id) DESC LIMIT :limit";
    private static final String GET_FILM_BY_ID_QUERY = "SELECT * FROM films WHERE id = :id";
    private static final String GET_GENRES_BY_ID_QUERY =
            "SELECT g.id, g.name FROM films_genres fg LEFT JOIN genres g ON fg.genre_id = g.id WHERE film_id = :id";
    private static final String GET_GENRES_QUERY = "SELECT * FROM genres";
    private static final String DELETE_FILM_QUERY = "DELETE FROM films WHERE film_name = :film_name";
    private static final String DELETE_GENRE_QUERY = "DELETE FROM films_genres WHERE film_id = :film_id";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes WHERE film_id = :film_id AND user_id = :user_id";
    private static final String GET_ALL_FILMS_QUERY = "SELECT * FROM films";

    @Override
    public Film add(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("release_date", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        params.addValue("mpa_id", film.getMpa().getId());
        jdbcTemplate.update(INSERT_FILM_QUERY, params, keyHolder, new String[]{"id"});

        film.setId(keyHolder.getKey().longValue());
        putGenres(film.getGenres(), film.getId());
        film.setGenres(film.getGenres()
                .stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        // у меня в тесте постмана передается 5,3 вместо 3,5
        return film;
    }

    @Override
    public void delete(long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource("film_id", id);
        jdbcTemplate.update(DELETE_GENRE_QUERY, parameterSource);
        jdbcTemplate.update(DELETE_FILM_QUERY, parameterSource);
    }

    @Override
    public Film update(Film film) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("film_id", film.getId());

        jdbcTemplate.update(DELETE_GENRE_QUERY, parameterSource);
        parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", film.getId());
        parameterSource.addValue("film_name", film.getName());
        parameterSource.addValue("description", film.getDescription());
        parameterSource.addValue("release_date", film.getReleaseDate());
        parameterSource.addValue("duration", film.getDuration());
        parameterSource.addValue("mpa_id", film.getMpa().getId());

        jdbcTemplate.update(UPDATE_FILM_QUERY, parameterSource);
        putGenres(film.getGenres(), film.getId());

        return film;
    }


    @Override
    public Optional<Film> getFilm(long id) {
        try {
            SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
            Film film = jdbcTemplate.queryForObject(GET_FILM_BY_ID_QUERY, parameterSource, new FilmRowMapper());
            List<Genre> genres = jdbcTemplate.query(GET_GENRES_BY_ID_QUERY, parameterSource, new GenreRowMapper());
            film.setGenres(new LinkedHashSet<>(genres));

            return Optional.ofNullable(film);
        } catch (EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException during query for film: {}", id);
            return Optional.empty();
        }
    }

    @Override
    public Collection<Film> getFilms(Optional<Integer> limit) {
        Map<Long, LinkedHashSet<Genre>> genres = new HashMap<>();
        List<Film> films;

        if (limit.isPresent()) {
            SqlParameterSource parameterSource = new MapSqlParameterSource("limit", limit.get());
            films = jdbcTemplate.query(GET_POPULAR_FILMS_QUERY, parameterSource, new FilmRowMapper());

        } else {
            films = jdbcTemplate.query(GET_ALL_FILMS_QUERY, new FilmRowMapper());
        }

        jdbcTemplate.query(GET_GENRES_QUERY, new GenreRowMapper());

        for (Film film : films) {
            if (genres.get(film.getId()) != null) {
                film.setGenres(genres.get(film.getId()));
            }
        }

        return films;
    }

    private void putGenres(Set<Genre> genres, long id) {
        MapSqlParameterSource[] batchParams = genres.stream()
                .map(genre -> {
                    MapSqlParameterSource p1 = new MapSqlParameterSource();
                    p1.addValue("genre_id", genre.getId());
                    p1.addValue("film_id", id);
                    return p1;
                })
                .toArray(MapSqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(INSERT_GENRE_QUERY, batchParams);
    }

    @Override
    public void setLike(long filmId, long userId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("film_id", filmId);
        parameterSource.addValue("user_id", userId);
        jdbcTemplate.update(INSERT_LIKE_QUERY, parameterSource);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("film_id", filmId);
        parameterSource.addValue("user_id", userId);
        jdbcTemplate.update(DELETE_LIKE_QUERY, parameterSource);
    }
}
