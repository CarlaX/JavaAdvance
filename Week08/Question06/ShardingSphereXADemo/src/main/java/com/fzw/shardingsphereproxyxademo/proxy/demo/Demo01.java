package com.fzw.shardingsphereproxyxademo.proxy.demo;

import com.fzw.shardingsphereproxyxademo.proxy.util.XAPooledUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author fzw
 * @description
 * @date 2021-06-29
 **/
@Slf4j
public class Demo01 {

    @Test
    public void test() {
        Connection connection = XAPooledUtil.getConnection();
        try (connection;) {
            connection.setAutoCommit(false);


            PreparedStatement preparedStatement = connection.prepareStatement("insert into `user`(username,password) values (?,?)");
            preparedStatement.setString(1, "carla");
            preparedStatement.setString(2, "carla");

            int updateRow = preparedStatement.executeUpdate();

            log.info("{}", updateRow);

            connection.commit();
        } catch (SQLException exception) {
            exception.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            XAPooledUtil.closePool();
        }
    }
}
