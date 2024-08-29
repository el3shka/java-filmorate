package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate")
class UserRepositoryApplicationTests {
    private final UserStorage userRepository;
    User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setLogin("login");
        user.setName("name");
        user.setEmail("email");
        user.setBirthday(LocalDate.of(2001, 1, 1));

        userRepository.add(user);
    }

    @Test
    public void couldFindUserById() {
        Optional<User> userOptional = userRepository.get(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user1 ->
                        assertThat(user1).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void couldUpdateUser() {
        user.setName("user2");
        userRepository.update(user);
        User user1 = userRepository.update(user);

        assertThat(user1).hasFieldOrPropertyWithValue("name", "user2");
    }

    @Test
    public void couldGetUsersList() {
        User user1 = new User();
        user1.setLogin("login1");
        user1.setName("name1");
        user1.setEmail("email1");
        user1.setBirthday(LocalDate.of(2001, 1, 1));
        userRepository.add(user1);

        List<User> users = userRepository.getUsers();

        assertThat(users).hasSize(2);
    }

    @Test
    public void couldGetUser() {
        Optional<User> newUser = userRepository.get(user.getId());

        assertThat(newUser).isPresent();
        assertEquals(user, newUser.get());
    }

    @Test
    public void couldSetFriend() {
        User user1 = new User();
        user1.setLogin("login2");
        user1.setName("name2");
        user1.setEmail("email2");
        user1.setBirthday(LocalDate.of(2001, 1, 1));

        user1 = userRepository.add(user1);

        userRepository.setFriend(user1.getId(), user.getId());

        assertEquals(user.getId(), userRepository.getFriends(user1.getId()).getFirst().getId());
    }

    @Test
    public void couldGetCommonFriends() {
        User user1 = new User();
        user1.setLogin("login1");
        user1.setName("name1");
        user1.setEmail("email1");
        user1.setBirthday(LocalDate.of(2001, 1, 1));

        User user2 = new User();
        user2.setLogin("login2");
        user2.setName("name2");
        user2.setEmail("email2");
        user2.setBirthday(LocalDate.of(2001, 1, 1));

        user1 = userRepository.add(user1);
        user2 = userRepository.add(user2);

        userRepository.setFriend(user1.getId(), user.getId());
        userRepository.setFriend(user2.getId(), user.getId());

        assertEquals(user.getId(), userRepository.getCommonFriends(user1.getId(), user2.getId()).getFirst().getId());
    }

}

