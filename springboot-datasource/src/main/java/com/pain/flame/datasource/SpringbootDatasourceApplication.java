package com.pain.flame.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SpringbootDatasourceApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SpringbootDatasourceApplication.class);

    // 查看 dataSource, jdbcTemplate bean
    // http://localhost:8080/actuator/beans

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDatasourceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        showConnection();
        showData();
    }

    private void showConnection() throws SQLException {
        logger.info("datasource: ", dataSource.toString());
        Connection connection = dataSource.getConnection();
        logger.info("connection: ", connection.toString());
        connection.close();
    }

    private void showData() {
        List<Map<String, Object>> blames = jdbcTemplate.queryForList("SELECT * FROM blame");
        for (Map blame : blames) {
            logger.info(blame.toString());
        }
    }
}
