package com.pain.flame.transaction;

import com.pain.flame.transaction.dao.UserDao;
import com.pain.flame.transaction.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Date;
import java.util.Random;

@EnableTransactionManagement(mode = AdviceMode.PROXY)
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private UserDao userDao;

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("start test, count: {}", userDao.count());

        userDao.insert(createUser());
        logger.info("after call insert method, count: {}", userDao.count());

        try {
            userDao.insertThenRollback(createUser());
        } catch (Exception ex) {
            logger.info("after call insertThenRollback method, count: {}", userDao.count());
        }

        try {
            userDao.insertThenRollbackV1(createUser());
        } catch (Exception ex) {
            logger.info("after call insertThenRollbackV1 method, count: {}", userDao.count());
        }

        try {
            userDao.insertThenRollbackV2(createUser());
        } catch (Exception ex) {
            logger.info("after call insertThenRollbackV2 method, count: {}", userDao.count());
        }

        try {
            userDao.insertThenRollbackV3(createUser());
        } catch (Exception ex) {
            logger.info("after call insertThenRollbackV3 method, count: {}", userDao.count());
        }

        try {
            userDao.insertThenRollbackV4(createUser());
        } catch (Exception ex) {
            logger.info("after call insertThenRollbackV4 method, count: {}", userDao.count());
        }
    }

    private static final Random random = new Random();

    private static User createUser() {

        User user = new User();
        user.setName("kafka" + random.nextInt(1000));
        user.setCreateTime(new Date());

        return user;
    }
}
