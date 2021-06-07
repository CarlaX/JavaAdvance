package com.fzw.beandemo.demo04;

import com.fzw.beandemo.pojo.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
@ComponentScan(basePackageClasses = {Demo04.class})
@Configuration
public class BeanConfig {
    @Bean
    public Person person() {
        return new Person("carla2", "neon2");
    }
}
