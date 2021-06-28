package com.fzw.shardingsphereproxydemo.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author fzw
 * @description
 **/
public class PooledUtil {
    private static final HikariDataSource dataSource;

    static {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(
                "jdbc:mysql://localhost:3316/sharding_db?" +
                        "serverTimezone=GMT%2B8&characterEncoding=utf8&useUnicode=true&useSSL=false&" +
                        "useAffectedRows=true&allowPublicKeyRetrieval=true&allowMultiQueries=true" +
                        "&rewriteBatchedStatements=true");
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("root");
        dataSource = new HikariDataSource(hikariConfig);
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            dataSource.evictConnection(connection);
        }

    }
}
