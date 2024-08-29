package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FriendStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class FriendsRowMapper implements RowMapper<Map<Long, Map<Long, FriendStatus>>> {
    private final Map<Long, Map<Long, FriendStatus>> friendsMap;

    @Override
    public Map<Long, Map<Long, FriendStatus>> mapRow(ResultSet rs, int rowNum) throws SQLException {
        long userId = rs.getLong("user_id");
        long friendId = rs.getLong("friend_id");
        FriendStatus fs = FriendStatus.valueOf(rs.getString("status"));

        if (friendsMap.containsKey(userId)) {
            friendsMap.get(userId).put(friendId, fs);
        } else {
            Map<Long, FriendStatus> m1 = new HashMap<>();
            m1.put(friendId, fs);
            friendsMap.put(userId, m1);
        }

        return friendsMap;
    }
}
