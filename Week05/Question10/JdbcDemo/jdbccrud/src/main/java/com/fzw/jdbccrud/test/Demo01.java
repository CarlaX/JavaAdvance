package com.fzw.jdbccrud.test;

import com.fzw.jdbccrud.util.JdbcUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
@Slf4j
public class Demo01 {

    @Test
    public void query() {
        try (Connection connection = JdbcUtil.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `order`");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int amount = resultSet.getInt("amount");
                log.info("{},{},{}", id, name, amount);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void insert() {
        try (Connection connection = JdbcUtil.getConnection();) {
            Statement statement = connection.createStatement();
            int insert = statement.executeUpdate("INSERT INTO `order`(name, amount) VALUES ('demo',111)");
            log.info("{}", insert);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void update() {
        try (Connection connection = JdbcUtil.getConnection();) {
            Statement statement = connection.createStatement();
            int update = statement.executeUpdate("UPDATE `order` SET name = 'ttt' WHERE id = 4");
            log.info("{}", update);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void delete() {
        try (Connection connection = JdbcUtil.getConnection();) {
            Statement statement = connection.createStatement();
            int delete = statement.executeUpdate("DELETE FROM `order` WHERE id = 4");
            log.info("{}", delete);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
