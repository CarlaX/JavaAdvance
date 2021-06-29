package com.fzw.shardingsphereproxyxademo.jdbc.demo;

import com.fzw.shardingsphereproxyxademo.jdbc.util.XAPooledUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.jdbc.core.connection.ShardingSphereConnection;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;

/**
 * @author fzw
 * @description
 * @date 2021-06-29
 **/
@Slf4j
public class Demo01 {
    @Test
    public void test() {
        ShardingSphereConnection connection = XAPooledUtil.getConnection();
        try (connection;) {
            connection.setAutoCommit(false);
            PreparedStatement userStatement = connection.prepareStatement("insert into `user`(username,password) values (?,?)", Statement.RETURN_GENERATED_KEYS);
            userStatement.setString(1, "carla");
            userStatement.setString(2, "carla");
            int userRow = userStatement.executeUpdate();
            ResultSet generatedKeys = userStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1);
                log.info("{}", id);
            }
            PreparedStatement orderStatement = connection
                    .prepareStatement("insert into `order`(user_id,total_price,total_amount,create_time)values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            orderStatement.setLong(1, 1L);
            orderStatement.setBigDecimal(2, new BigDecimal("2.22"));
            orderStatement.setInt(3, 2);
            orderStatement.setTimestamp(4, Timestamp.from(Instant.now()));
            int orderRow = orderStatement.executeUpdate();
            ResultSet generatedKeys_2 = orderStatement.getGeneratedKeys();
            if (generatedKeys_2.next()) {
                long id = generatedKeys_2.getLong(1);
                log.info("{}", id);
            }
            connection.commit();
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            exception.printStackTrace();
        }
    }
}
