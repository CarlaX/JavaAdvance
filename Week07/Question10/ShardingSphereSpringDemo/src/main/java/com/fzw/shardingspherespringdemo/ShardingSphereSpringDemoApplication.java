package com.fzw.shardingspherespringdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan(basePackages = {"com.fzw.shardingspherespringdemo.mapper"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ShardingSphereSpringDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingSphereSpringDemoApplication.class, args);
    }

}
