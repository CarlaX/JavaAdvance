package com.fzw.jdbccrud.test;

import com.fzw.jdbccrud.util.JdbcUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Arrays;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
@Slf4j
public class Demo02 {

    @Test
    public void query() {
        try (Connection connection = JdbcUtil.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `order`");
            ResultSet resultSet = preparedStatement.executeQuery();
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
        Connection connection = null;
        try {
            connection = JdbcUtil.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `order`(name, amount) VALUES (?,?)");
            for (int i = 0; i < 10; i++) {
                preparedStatement.setString(1, "demo_" + i);
                preparedStatement.setInt(2, i);
                preparedStatement.addBatch();
            }
            int[] ints = preparedStatement.executeBatch();
            connection.commit();
            log.info("{}", Arrays.toString(ints));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        }
    }

    @Test
    public void update() {
        try (Connection connection = JdbcUtil.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `order` SET name = 'ttt' WHERE id = ?");
            for (int i = 0; i < 10; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.addBatch();
            }
            int[] ints = preparedStatement.executeBatch();
            log.info("{}", Arrays.toString(ints));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void delete() {
        try (Connection connection = JdbcUtil.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `order` WHERE id = ?");
            for (int i = 35; i < 45; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.addBatch();
            }
            int[] ints = preparedStatement.executeBatch();
            log.info("{}", Arrays.toString(ints));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
