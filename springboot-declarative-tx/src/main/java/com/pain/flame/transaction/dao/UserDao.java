package com.pain.flame.transaction.dao;

import com.pain.flame.transaction.Exception.RollbackException;
import com.pain.flame.transaction.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDao userDao;

    @Transactional
    public void insert(User user) {
        jdbcTemplate.update("insert into user(name, create_time) values(?, ?)", user.getName(), user.getCreateTime());
    }


    /**
     * 1. Propagation.REQUIRES_NEW, 始终启动一个新事务，新旧事务没有关联
     * 2. Propagation.NESTED, 两个事务有关联。外部事务回滚，内嵌事务也会回滚
     * @throws RollbackException
     */

    @Transactional(rollbackFor = RollbackException.class, propagation = Propagation.NESTED)
    public void insertThenRollback(User user) throws RollbackException {
        jdbcTemplate.update("insert into user(name, create_time) values(?, ?)", user.getName(), user.getCreateTime());
        throw new RollbackException();
    }

    public void insertThenRollbackV1(User user) throws RollbackException {
        insertThenRollback(user);
    }

    public void insertThenRollbackV2(User user) throws RollbackException {
        userDao.insertThenRollback(user);
    }

    public void insertThenRollbackV3(User user) throws RollbackException {
        ((UserDao)AopContext.currentProxy()).insertThenRollback(user);
    }

    @Transactional(rollbackFor = RollbackException.class)
    public void insertThenRollbackV4(User user) throws RollbackException {
        insertThenRollback(user);
    }

    public long count() {
        Long count = jdbcTemplate.queryForObject("select count(*) from user", Long.class);
        return count;
    }
}
