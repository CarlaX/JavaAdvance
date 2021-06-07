package com.fzw.jdbccrud.test;

import com.fzw.jdbccrud.util.PooledUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
@Slf4j
public class Demo03 {


    @Test
    public void query() {
        Connection connection = null;
        try {
            connection = PooledUtil.getConnection();
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
        } finally {
            PooledUtil.releaseConnection(connection);
        }
    }

    @Test
    public void insert() {
        Connection connection = null;
        try {
            connection = PooledUtil.getConnection();
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
            PooledUtil.releaseConnection(connection);
        }
    }

    @Test
    public void update() {
        Connection connection = null;
        try {
            connection = PooledUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `order` SET name = 'ttt' WHERE id = ?");
            for (int i = 45; i < 55; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.addBatch();
            }
            int[] ints = preparedStatement.executeBatch();
            log.info("{}", Arrays.toString(ints));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            PooledUtil.releaseConnection(connection);
        }
    }

    @Test
    public void delete() {
        Connection connection = null;
        try {
            connection = PooledUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `order` WHERE id = ?");
            for (int i = 35; i < 45; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.addBatch();
            }
            int[] ints = preparedStatement.executeBatch();
            log.info("{}", Arrays.toString(ints));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            PooledUtil.releaseConnection(connection);
        }
    }

}
