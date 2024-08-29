package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FriendsIdsRowMapper;
import ru.yandex.practicum.filmorate.mapper.FriendsRowMapper;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Primary
@RequiredArgsConstructor
@Repository("userRepository")
public class UserRepository implements UserStorage {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(login, email, name, birthday) VALUES (:login, :email, :name, :birthday)";
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET login = :login, email = :email, name = :name, birthday = :birthday WHERE id = :id";
    private static final String GET_USER_QUERY = "SELECT * FROM users WHERE id = :id";
    private static final String SET_FRIENDS_QUERY =
            "INSERT INTO friends(user_id, friend_id, status) VALUES (:user_id, :friend_id, :status)";
    private static final String GET_USERS_QUERY = "SELECT * FROM users";
    private static final String GET_FRIENDS_QUERY = "SELECT * FROM friends";
    private static final String DELETE_FRIEND_QUERY =
            "DELETE FROM friends WHERE user_id = :user_id AND friend_id = :friend_id";
    private static final String GET_USERS_FRIENDS =
            "SELECT * FROM users WHERE id IN(SELECT DISTINCT user_id FROM friends WHERE friend_id = :friend_id)";
    private static final String DELETE_FRIENDS_QUERY = "DELETE FROM friends WHERE user_id = :user_id";
    private static final String GET_COMMON_FRIENDS_QUERY = "SELECT *" +
            "FROM users " +
            "WHERE id IN (SELECT user_id FROM friends WHERE friend_id = :user_id " +
            "INTERSECT " +
            "SELECT user_id FROM friends WHERE friend_id = :friend_id)";
    private static final String GET_FRIENDS_IDS_QUERY = "SELECT * FROM friends WHERE user_id = :user_id";

    @Override
    public User add(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("login", user.getLogin());
        params.addValue("email", user.getEmail());
        params.addValue("name", user.getName());
        params.addValue("birthday", user.getBirthday());

        jdbcTemplate.update(CREATE_USER_QUERY, params, keyHolder, new String[]{"id"});
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        MapSqlParameterSource p1 = new MapSqlParameterSource("user_id", user.getId());

        jdbcTemplate.update(DELETE_FRIENDS_QUERY, p1);

        params.addValue("id", user.getId());
        params.addValue("login", user.getLogin());
        params.addValue("email", user.getEmail());
        params.addValue("name", user.getName());
        params.addValue("birthday", user.getBirthday());

        jdbcTemplate.update(UPDATE_USER_QUERY, params);
        putFriends(user.getFriends(), user.getId());

        return user;
    }

    private void putFriends(Map<Long, FriendStatus> map, long id) {
        MapSqlParameterSource[] batchParams = map.entrySet().stream()
                .map(entry -> {
                    MapSqlParameterSource p1 = new MapSqlParameterSource();
                    p1.addValue("user_id", id);
                    p1.addValue("friend_id", entry.getKey());
                    p1.addValue("status", entry.getValue().toString());
                    return p1;
                })
                .toArray(MapSqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(SET_FRIENDS_QUERY, batchParams);
    }

    @Override
    public void delete(long userId, long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", friendId);
        params.addValue("friend_id", userId);

        jdbcTemplate.update(DELETE_FRIEND_QUERY, params);
    }

    @Override
    public Optional<User> get(long userId) throws EmptyResultDataAccessException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource("id", userId);
            User user = jdbcTemplate.queryForObject(GET_USER_QUERY, params, new UserRowMapper());
            user.setFriends(getFriends(userId, new MapSqlParameterSource("user_id", userId)));

            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            log.info("EmptyResultDataAccessException during query for user: {}", userId);
            return Optional.empty();
        }

    }

    private Map<Long, FriendStatus> getFriends(long userId, MapSqlParameterSource params) {
        List<Map.Entry<Long, FriendStatus>> entries;
        try {
            entries = jdbcTemplate.query(GET_FRIENDS_IDS_QUERY, params, new FriendsIdsRowMapper());
            return entries.stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public List<User> getUsers() {
        List<User> users = jdbcTemplate.query(GET_USERS_QUERY, new UserRowMapper());
        Map<Long, Map<Long, FriendStatus>> usersFriendsMap = new HashMap<>();

        jdbcTemplate.query(GET_FRIENDS_QUERY, new FriendsRowMapper(usersFriendsMap));
        for (var user : users) {
            if (usersFriendsMap.containsKey(user.getId())) {
                user.setFriends(usersFriendsMap.get(user.getId()));
            }
        }
        return users;
    }


    @Override
    public User setFriend(long userId, long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", friendId);
        params.addValue("friend_id", userId);
        params.addValue("status", FriendStatus.UNCONFIRMED.toString());

        jdbcTemplate.update(SET_FRIENDS_QUERY, params);

        return get(userId).get();
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("friend_id", friendId);
        List<User> users = jdbcTemplate.query(GET_COMMON_FRIENDS_QUERY, params, new UserRowMapper());
        return users;
    }

    @Override
    public List<User> getFriends(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource("friend_id", id);
        return jdbcTemplate.query(GET_USERS_FRIENDS, params, new UserRowMapper());
    }
}
