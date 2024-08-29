package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.annotation.NoSpaceInLogin;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class User {
    private long id;

    @NotNull
    @NotBlank
    @NoSpaceInLogin
    private String login;

    @NotNull
    @NotBlank
    @Email
    private String email;

    private String name;

    @Past
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private Map<Long, FriendStatus> friends = new HashMap();
}
