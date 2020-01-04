package com.pain.flame.jdbc;

import com.pain.flame.jdbc.dao.UserDao;
import com.pain.flame.jdbc.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private UserDao userDao;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        testBatchInsert();
        testList();
    }

    private void testSingleInsert() {
        Random random = new Random();
        User user = new User();
        user.setName("jack" + random.nextInt(1000));
        user.setCreateTime(new Date());

        userDao.insert(user);

        Map userMap1 = new HashMap();
        userMap1.put("name", "pain" + random.nextInt(1000));
        userMap1.put("create_time", new Date());

        userDao.insertV1(userMap1);

        Map userMap2 = new HashMap();
        userMap2.put("name", "page" + random.nextInt(1000));
        userMap2.put("createTime", new Date());

        userDao.insertV2(userMap2);
    }

    private void testBatchInsert() {
        List<User> userList1 = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 10; ++i) {
            User user = new User();
            user.setName("blade" + random.nextInt(1000));
            user.setCreateTime(new Date());
            userList1.add(user);
        }

        userDao.batchInsertV1(userList1);

        List<User> userList2 = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            User user = new User();
            user.setName("plot" + random.nextInt(1000));
            user.setCreateTime(new Date());
            userList2.add(user);
        }

        userDao.batchInsertV2(userList2);
    }

    private void testList() {
        userDao.count();
        userDao.listName();
        userDao.listUser();
    }
}
