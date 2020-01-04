package com.pain.flame.jdbc.dao;

import com.pain.flame.jdbc.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Bean
    public SimpleJdbcInsert simpleJdbcInsert(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate).withTableName("user").usingGeneratedKeyColumns("id");
    }

    public void insert(User user) {
        jdbcTemplate.update("insert into user(name, create_time) values (?, ?)", user.getName(), new Date());
    }

    public void insertV0(User user) {
        Object[] params = new Object[]{user.getName(), user.getCreateTime()};
        jdbcTemplate.update("insert into user(name, create_time) values (?, ?)", params);
    }

    public void insertV1(Map userMap) {
        Number number = simpleJdbcInsert.executeAndReturnKey(userMap);
        logger.info("insert and return id: {}", number.intValue());
    }

    public void insertV2(Map userMap) {
        namedParameterJdbcTemplate.update("insert into user(name, create_time) values (:name, :createTime)", userMap);
    }

    public void insertV3(User user) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        namedParameterJdbcTemplate.update("insert into user(name, create_time) values (:name, :createTime)", parameterSource);
    }

    public void insertV4(User user) {
        SqlParameterSource parameterSource = new MapSqlParameterSource();
        ((MapSqlParameterSource) parameterSource).addValue("name", user.getName());
        ((MapSqlParameterSource) parameterSource).addValue("createTime", user.getCreateTime());
        namedParameterJdbcTemplate.update("insert into user(name, create_time) values (:name, :createTime)", parameterSource);
    }

    public void batchInsertV1(List<User> userList) {

        // 如果希望将一个 List 中的数据批量更新到数据库中，getBatchSize 设置为 List 的大小
        // 如果 List 非常大，希望多次批量提交，可以分段读取将大的 List 暂存到小 List 中，再将这个小 List 批量保存到数据库中
        jdbcTemplate.batchUpdate("insert into user(name, create_time) values (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, userList.get(i).getName());
                preparedStatement.setTimestamp(2, new Timestamp(userList.get(i).getCreateTime().getTime()));
            }

            @Override
            public int getBatchSize() {
                return userList.size();
            }
        });
    }

    public void batchInsertV2(List<User> userList) {
        namedParameterJdbcTemplate.batchUpdate("insert into user(name, create_time) values (:name, :createTime)",
                SqlParameterSourceUtils.createBatch(userList));
    }

    public long count() {
        Long count = jdbcTemplate.queryForObject("select count(*) from user", Long.class);
        logger.info("user count: {}", count);

        return count;
    }

    public User getById(long id) {
        Object[] params = new Object[]{id};

        User user = jdbcTemplate.queryForObject("select * from user where id = ?", params, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User u = new User();
                u.setId(resultSet.getInt(0));
                u.setName(resultSet.getString(1));
                u.setCreateTime(resultSet.getTimestamp(2));
                return u;
            }
        });

        return user;
    }

    public List<String> listName() {
        List<String> names = jdbcTemplate.queryForList("select name from user", String.class);

        for (String name : names) {
            logger.info("name: {}", name);
        }

        return names;
    }

    public List<User> listUser() {

        // 当处理大结果集时，如果使用 RowMapper<T> 接口，采用的方式是将结果集中的所有数据放到 List 对象中，会占用大量 JVM 内存
        List<User> userList = jdbcTemplate.query("select * from user", new RowMapper<User>() {
            @SuppressWarnings("Duplicates")
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setCreateTime(resultSet.getTimestamp("create_time"));
                return user;
            }
        });

        for (User user : userList) {
            logger.info("user: {}", user);
        }

        return userList;
    }

    public List<User> listUserByIds(List<Long> ids) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("ids", ids);

        List<User> userList = namedParameterJdbcTemplate.query("select * from user where id in (:ids)", parameterSource, new RowMapper<User>() {
            @SuppressWarnings("Duplicates")
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setCreateTime(resultSet.getTimestamp("create_time"));
                return user;
            }
        });

        for (User user : userList) {
            logger.info("user: {}", user);
        }

        return userList;
    }
}
