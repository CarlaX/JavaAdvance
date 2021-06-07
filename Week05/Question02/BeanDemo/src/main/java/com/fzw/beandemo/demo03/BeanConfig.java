package com.fzw.beandemo.demo03;

import com.fzw.beandemo.demo04.Demo04;
import com.fzw.beandemo.pojo.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
@Configuration
public class BeanConfig {
    @Bean
    public Person person() {
        return new Person("carla2", "neon2");
    }
}
