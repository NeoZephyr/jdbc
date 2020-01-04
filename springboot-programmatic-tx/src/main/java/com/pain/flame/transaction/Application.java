package com.pain.flame.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private long getUserCount() {
        Long count = jdbcTemplate.queryForObject("select count(*) from user", Long.class);
        return count;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("before transaction, count: {}", getUserCount());

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                jdbcTemplate.update("insert into user(name, create_time) values(?, ?)", "pain-tx", new Date());
                logger.info("in transaction, count: {}", getUserCount());
                transactionStatus.setRollbackOnly();
            }
        });

        logger.info("after transaction, count: {}", getUserCount());
    }
}
