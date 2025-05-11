package com.andymur.projectw.repository;

import com.andymur.projectw.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User(rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"));
        return user;
    };

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper);
    }

    public int save(User user) {
        return jdbcTemplate.update("INSERT INTO users (name, email) VALUES (?, ?)",
                user.getName(), user.getEmail());
    }
}

