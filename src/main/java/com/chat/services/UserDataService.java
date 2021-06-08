package com.chat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDataService {

        private final JdbcTemplate jdbcTemplate;

        @Autowired
        public UserDataService(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        List<String> getUsers() {
                return jdbcTemplate.query("SELECT * FROM chat.get_users()", mapUsersFromDb());
        }

        void setUser(String userName) {
                jdbcTemplate.update("CALL chat.insert_user(?)", userName);
        }

        boolean isUserNameTaken(String userName) {
                return jdbcTemplate.queryForObject("SELECT * FROM chat.is_name_taken(?)",
                                (resultSet, i) -> resultSet.getBoolean(1), new Object[] { userName });
        }

        private RowMapper<String> mapUsersFromDb() {
                return (resultSet, i) -> {
                        String userName = resultSet.getString("user_name");
                        return userName;
                };
        }
}
