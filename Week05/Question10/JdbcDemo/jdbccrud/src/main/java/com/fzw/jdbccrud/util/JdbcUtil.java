package com.fzw.jdbccrud.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
@Slf4j
public class JdbcUtil {
    private static final String driverClass = "com.mysql.cj.jdbc.Driver";
    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/orderserver?serverTimezone=GMT%2B8&characterEncoding=utf8&useUnicode=true&useSSL=false&useAffectedRows=true&allowPublicKeyRetrieval=true&allowMultiQueries=true";
    private static final String username = "carla";
    private static final String password = "carla";

    static {
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

//    public static void closeConnection(Connection connection) {
//        try {
//            connection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }
}
