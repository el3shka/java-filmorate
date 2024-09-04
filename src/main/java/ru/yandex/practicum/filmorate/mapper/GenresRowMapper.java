package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Map;

@RequiredArgsConstructor
public class GenresRowMapper implements RowMapper<Map<Long, LinkedHashSet<Genre>>> {
    private final Map<Long, LinkedHashSet<Genre>> genres;

    @Override
    public Map<Long, LinkedHashSet<Genre>> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long filmId = rs.getLong("film_id");
        Long genreId = rs.getLong("genre_id");

        if (genres.containsKey(filmId)) {
            genres.get(filmId).add(new Genre(genreId, null));
        } else {
            LinkedHashSet<Genre> set = new LinkedHashSet<>();
            set.add(new Genre(genreId, null));
            genres.put(filmId, set);
        }
        return genres;
    }
}
