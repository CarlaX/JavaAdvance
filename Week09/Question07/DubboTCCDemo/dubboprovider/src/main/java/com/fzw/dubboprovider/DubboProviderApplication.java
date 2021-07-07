package com.fzw.dubboprovider;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author fzw
 * @description
 * @date 2021-07-06
 **/
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan(basePackages = {"com.fzw.dubboprovider.mapper"})
@SpringBootApplication(scanBasePackages = {"com.fzw.*"})
public class DubboProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(DubboProviderApplication.class, args);
    }
}
