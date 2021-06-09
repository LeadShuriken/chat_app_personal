package com.chat.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class ChatRoomRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChatRoomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
