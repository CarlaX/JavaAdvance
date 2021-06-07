package com.fzw.jdbccrud.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
public class PooledUtil {
    private static final HikariDataSource dataSource;

    static {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(
                "jdbc:mysql://localhost:3306/orderserver?serverTimezone=GMT%2B8&characterEncoding=utf8&useUnicode=true&useSSL=false&useAffectedRows=true&allowPublicKeyRetrieval=true&allowMultiQueries=true");
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setUsername("carla");
        hikariConfig.setPassword("carla");
        dataSource = new HikariDataSource(hikariConfig);
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            dataSource.evictConnection(connection);
        }

    }
}
