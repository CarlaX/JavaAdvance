package com.fzw.jdbccrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JdbcCrudApplication {
    public static void main(String[] args) {
        SpringApplication.run(JdbcCrudApplication.class, args);
    }
}
