package com.chat.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TokenDataRepository {

        private final JdbcTemplate jdbcTemplate;

        @Autowired
        public TokenDataRepository(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        void setToken(String token) {
                jdbcTemplate.update("INSERT INTO chat.token ( token_name ) VALUES (?)", token);
        }

        void removeToken(String token) {
                jdbcTemplate.update("DELETE FROM chat.token WHERE token_name = ?", token);
        }

        boolean tokenExists(String token) {
                return jdbcTemplate.queryForObject("SELECT EXISTS ( SELECT 1 FROM chat.token WHERE token_name = ? )",
                                (resultSet, i) -> resultSet.getBoolean(1), new Object[] { token });
        }

        int getTokenCount() {
                return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat.token",
                                (resultSet, i) -> resultSet.getInt(1), new Object[] {});
        }
}
