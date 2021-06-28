package com.fzw.shardingsphereproxydemo.demo;

import com.fzw.shardingsphereproxydemo.util.PooledUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author fzw
 * @description
 * @date 2021-06-28
 **/
@Slf4j
public class Demo01 {

    private Connection connection;

    @BeforeEach
    public void before() {
        this.connection = PooledUtil.getConnection();
    }

    @AfterEach
    public void after() {
        PooledUtil.releaseConnection(this.connection);
    }

    @Test
    public void test() {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("CREATE TABLE `user` (\n" +
                    "  `id` int NOT NULL,\n" +
                    "  `username` varchar(255) NOT NULL,\n" +
                    "  `password` varchar(255) NOT NULL,\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
            int update = preparedStatement.executeUpdate();
            log.info("{}", update);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void test02() {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("insert into `user` (username,password) values ('carla','carla')");
            int update = preparedStatement.executeUpdate();
            log.info("{}", update);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void test03() {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("update `user` set password = '123' where username = 'carla'");
            int update = preparedStatement.executeUpdate();
            log.info("{}", update);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void test04() {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("update `user` set password = '456' where id = 616283292668768256");
            int update = preparedStatement.executeUpdate();
            log.info("{}", update);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void test05() {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("select * from `user`");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                log.info("{},{},{}", id, username, password);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
