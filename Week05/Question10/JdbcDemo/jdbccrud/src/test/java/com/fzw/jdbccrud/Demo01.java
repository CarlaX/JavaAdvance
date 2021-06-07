package com.fzw.jdbccrud;

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
@SpringBootTest
public class Demo01 {

    @Test
    public void test() {
        try (Connection connection = JdbcUtil.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from `order`");
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
}
