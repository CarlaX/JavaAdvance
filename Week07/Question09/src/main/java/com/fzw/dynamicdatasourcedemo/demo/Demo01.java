package com.fzw.dynamicdatasourcedemo.demo;

import com.fzw.dynamicdatasourcedemo.config.DynamicDatasource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;
import java.util.Properties;

/**
 * @author fzw
 * @description
 * @date 2021-06-24
 **/
@Slf4j
@SpringBootTest
public class Demo01 {

    @Autowired
    private DynamicDatasource dynamicDatasource;

    @Test
    public void test01() {
        Connection connection = null;
        try {
            DynamicDatasource.setKeyContextContent(DynamicDatasource.MASTER_KEY);
            connection = this.dynamicDatasource.getConnection();

            DatabaseMetaData metaData = connection.getMetaData();
            String url = metaData.getURL();
            log.info("{}", url);

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into user (username, password) values (?,?)");
            preparedStatement.setString(1, "carla");
            preparedStatement.setString(2, "carla");
            int insertRow = preparedStatement.executeUpdate();
            log.info("插入条数：{}", insertRow);
            connection.commit();
        } catch (SQLException e) {
            try {
                e.printStackTrace();
                connection.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        } finally {
            DynamicDatasource.clearKeyContextContent();
        }
    }

    @Test
    public void test02() {
        Connection connection = null;
        try {
            DynamicDatasource.setKeyContextContent(DynamicDatasource.SLAVE_KEY);
            connection = this.dynamicDatasource.getConnection();

            DatabaseMetaData metaData = connection.getMetaData();
            String url = metaData.getURL();
            log.info("{}", url);

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from user");
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");

            log.info("用户名：{},密码：{}", username, password);

            connection.commit();
        } catch (SQLException e) {
            try {
                e.printStackTrace();
                connection.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        } finally {
            DynamicDatasource.clearKeyContextContent();
        }
    }
}
