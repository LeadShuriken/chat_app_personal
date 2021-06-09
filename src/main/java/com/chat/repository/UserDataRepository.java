package com.chat.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDataRepository {

        private final JdbcTemplate jdbcTemplate;

        @Autowired
        public UserDataRepository(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        List<String> getUsers() {  
                return jdbcTemplate.query("SELECT * FROM chat.users", mapUsersFromDb());
        }

        void setUser(String userName) {
                jdbcTemplate.update("INSERT INTO chat.users ( name ) VALUES (?)", userName);
        }

        boolean isUserNameTaken(String userName) {
                return jdbcTemplate.queryForObject("SELECT EXISTS ( SELECT 1 FROM chat.users WHERE userName = ? )",
                                (resultSet, i) -> resultSet.getBoolean(1), new Object[] { userName });
        }

        private RowMapper<String> mapUsersFromDb() {
                return (resultSet, i) -> {
                        String userName = resultSet.getString("user_name");
                        return userName;
                };
        }
}
