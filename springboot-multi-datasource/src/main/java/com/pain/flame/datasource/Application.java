package com.pain.flame.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 排除自动配置类
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class
})
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @ConfigurationProperties("apple.datasource")
    public DataSourceProperties orangeDatasourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("orange.datasource")
    public DataSourceProperties appleDatasourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Resource
    public DataSource orangeDatasource() {
        DataSourceProperties dataSourceProperties = orangeDatasourceProperties();

        logger.info("orange datasource: {}", dataSourceProperties.getUrl());
        DataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().build();
        return dataSource;
    }

    @Bean
    @Resource
    public DataSource appleDatasource() {
        DataSourceProperties dataSourceProperties = appleDatasourceProperties();

        logger.info("apple datasource: {}", dataSourceProperties.getUrl());
        DataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().build();
        return dataSource;
    }

    @Bean
    @Resource
    public PlatformTransactionManager orangeTxManager(DataSource orangeDatasource) {
        return new DataSourceTransactionManager(orangeDatasource);
    }

    @Bean
    @Resource
    public PlatformTransactionManager appleTxManager(DataSource appleDatasource) {
        return new DataSourceTransactionManager(appleDatasource);
    }
}
