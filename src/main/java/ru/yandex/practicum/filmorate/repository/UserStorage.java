package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User add(User user);

    User update(User user);

    void delete(long userId, long friendId);

    Optional<User> get(long userId);

    List<User> getUsers();

    List<User> getFriends(long id);

    User setFriend(long userId, long friendId);

    List<User> getCommonFriends(long userId, long friendId);
}
