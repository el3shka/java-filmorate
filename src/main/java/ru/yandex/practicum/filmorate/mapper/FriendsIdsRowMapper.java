package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FriendStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Map;

public class FriendsIdsRowMapper implements RowMapper<Map.Entry<Long, FriendStatus>> {

    @Override
    public Map.Entry<Long, FriendStatus> mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AbstractMap.SimpleEntry<>(
                rs.getLong("friend_id"),
                FriendStatus.valueOf(rs.getString("status")));
    }
}
