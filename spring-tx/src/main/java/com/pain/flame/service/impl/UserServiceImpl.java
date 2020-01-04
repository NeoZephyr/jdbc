package com.pain.flame.service.impl;

import com.pain.flame.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class UserServiceImpl implements UserService {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String getProfile() {

        jdbcTemplate.update("update user set name = ? where id = ?", "jook", 1);

        return "hello";
    }

    public void updateProfile() {
        jdbcTemplate.update("update user set name = ? where id = ?", "tuck", 1);
        System.out.println(1 / 0);
        jdbcTemplate.update("update user set name = ? where id = ?", "ruck", 2);
    }
}
