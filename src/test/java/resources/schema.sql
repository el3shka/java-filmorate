CREATE TABLE IF NOT EXISTS mpa
(
    id   integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(30),
    UNIQUE (name)
    );

CREATE TABLE IF NOT EXISTS films
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name    varchar(40),
    description  varchar(300),
    release_date date NOT NULL,
    duration     integer,
    mpa_id       integer,
    FOREIGN KEY (mpa_id) REFERENCES mpa (id)
    );
CREATE TABLE IF NOT EXISTS genres
(
    id   integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(30),
    UNIQUE (name)
    );

CREATE TABLE IF NOT EXISTS films_genres
(
    film_id  integer,
    genre_id integer,
    UNIQUE (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films (id),
    FOREIGN KEY (genre_id) REFERENCES genres (id)
    );


CREATE TABLE IF NOT EXISTS users
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login    varchar(20) NOT NULL,
    email    varchar(50) NOT NULL,
    name     varchar(20),
    birthday date,
    UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS likes
(
    film_id integer NOT NULL,
    user_id integer NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    UNIQUE (film_id, user_id)
    );

CREATE TABLE IF NOT EXISTS friends
(
    user_id   integer NOT NULL,
    friend_id integer NOT NULL,
    status    varchar(11),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (friend_id) REFERENCES users (id),
    UNIQUE (user_id, friend_id)
    );
