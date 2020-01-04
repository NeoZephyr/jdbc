package com.pain.flame.datasource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class Application {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        showBeans(context);
        dataSourceDemo(context);
        testRawConnection();
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("driverClassName", "com.mysql.jdbc.Driver");
        properties.setProperty("url", "jdbc:mysql://127.0.0.1:3306/orange?characterEncoding=utf-8&useSSL=false");
        properties.setProperty("username", "mysql");
        properties.setProperty("password", "123456");
        return BasicDataSourceFactory.createDataSource(properties);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() throws Exception {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        return new DataSourceTransactionManager(dataSource());
    }

    public void showDataSource() throws SQLException {
        System.out.println("dataSource: " + dataSource.toString());
        Connection connection = dataSource.getConnection();
        System.out.println("connection: " + connection.toString());
        connection.close();
    }

    private static void showBeans(ApplicationContext context) {
        System.out.println("beans: " + Arrays.toString(context.getBeanDefinitionNames()));
    }

    private static void dataSourceDemo(ApplicationContext context) throws SQLException {
        Application application = context.getBean("application", Application.class);
        application.showDataSource();
        application.testDataSource();
    }

    private void testDataSource() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from user where id = ?");
        statement.setInt(1, 18);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            Date createTime = resultSet.getTimestamp("create_time");
            System.out.println("name: " + name);
            System.out.println("createTime: " + createTime);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }

    private static void testRawConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/orange?characterEncoding=utf-8&useSSL=false", "mysql", "123456");
        Statement statement = connection.createStatement();
        String sql = "select * from user where id = 19";
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            Date createTime = resultSet.getTimestamp("create_time");
            System.out.println("name: " + name);
            System.out.println("createTime: " + createTime);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }
}
