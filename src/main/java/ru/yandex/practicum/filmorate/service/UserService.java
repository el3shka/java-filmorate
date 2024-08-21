package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public User createUser(final User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            user.setName(user.getLogin());
        }
        User user1 = userStorage.add(user);

        log.info("User created: {}", user);
        return user1;
    }

    public User updateUser(final User user) {
        if (userStorage.get(user.getId()).isEmpty()) {
            log.info("User update failed with noSuchElementException: {}", user);
            throw new NotFoundException("Такого пользователя нет!");
        }

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            user.setName(user.getLogin());
        }

        userStorage.update(user);
        log.info("User {} updated", user);
        return user;

    }

    public List<User> getUsers() {
        log.info("Users list was got");
        return new ArrayList<>(userStorage.getUsers());
    }

    public User setFriend(final long userId, final long friendId) {
        if (userStorage.get(userId).isEmpty()) {
            throw new NotFoundException("User with id " + userId + " does not exist");
        }
        if (userStorage.get(friendId).isEmpty()) {
            throw new NotFoundException("Friend with id " + friendId + " does not exist");
        }

        log.info("Users {} {} are friends now!", userId, friendId);
        return userStorage.setFriend(userId, friendId);
    }

    public User deleteFriend(final long userId, final long friendId) {
        if (userStorage.get(userId).isEmpty()) {
            throw new NotFoundException("User with id " + userId + " does not exist");
        }
        if (userStorage.get(friendId).isEmpty()) {
            throw new NotFoundException("Friend with id " + friendId + " does not exist");
        }

        userStorage.delete(userId, friendId);
        log.info("Users {} {} are not friends now", userId, friendId);
        return userStorage.get(userId).get();
    }

    public List<User> getFriends(final long userId) {
        if (userStorage.get(userId).isEmpty()) {
            throw new NotFoundException("User with id " + userId + " does not exist");
        }

        List<User> friends = userStorage.getFriends(userId);
        log.info("friend were taken");
        return friends;
    }

    public List<User> getCommonFriends(final long userId, final long friendId) {
        if (userStorage.get(userId).isEmpty()) {
            throw new NotFoundException("User with id " + userId + " does not exist");
        }
        if (userStorage.get(friendId).isEmpty()) {
            throw new NotFoundException("Friend with id " + friendId + " does not exist");
        }

        return userStorage.getCommonFriends(userId, friendId);
    }
}
