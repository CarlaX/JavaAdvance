package com.fzw.shardingsphereproxyxademo.demo;

import com.fzw.shardingsphereproxyxademo.util.XAPooledUtil;
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
        Connection connection = XAPooledUtil.getConnection(XAPooledUtil.MASTER);
        Connection connection_2 = XAPooledUtil.getConnection(XAPooledUtil.SLAVE);
        try (connection; connection_2;) {
            connection.setAutoCommit(false);
            connection_2.setAutoCommit(false);

            XAPooledUtil.beginXA();

            PreparedStatement preparedStatement = connection.prepareStatement("insert into `user`(id,username,password) values (?,?,?)");
            preparedStatement.setLong(1, 1L);
            preparedStatement.setString(2, "carla");
            preparedStatement.setString(3, "carla");

            PreparedStatement preparedStatement_2 = connection_2.prepareStatement("insert into `user`(id,username,password) values (?,?,?)");
            preparedStatement_2.setLong(1, 1L);
            preparedStatement_2.setString(2, "neon");
            preparedStatement_2.setString(3, "neon");

            int updateRow = preparedStatement.executeUpdate();
            int updateRow_2 = preparedStatement_2.executeUpdate();

            log.info("{},{}", updateRow, updateRow_2);
            XAPooledUtil.commitXA();
        } catch (SQLException exception) {
            exception.printStackTrace();
            XAPooledUtil.rollbackXA();
        }
    }

}
