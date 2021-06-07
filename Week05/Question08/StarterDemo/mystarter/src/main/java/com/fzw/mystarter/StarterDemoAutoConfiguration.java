package com.fzw.mystarter;

import com.fzw.mystarter.config.StarterDemoConfig;
import com.fzw.mystarter.pojo.Klass;
import com.fzw.mystarter.pojo.School;
import com.fzw.mystarter.pojo.Student;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
@Configuration
@ComponentScan(basePackages = {"com.fzw.mystarter"})
@EnableConfigurationProperties(value = {StarterDemoConfig.class})
@ConditionalOnProperty(prefix = "starterdemo.starter", name = "enabled", havingValue = "true")
public class StarterDemoAutoConfiguration {

//    @Bean
//    public StarterDemoConfig starterDemoConfig() {
//        return new StarterDemoConfig();
//    }

    @Bean
    public Student student01() {
        return new Student(1, "stu01");
    }

    @Bean
    public Student student02() {
        return new Student(2, "stu02");
    }

    @Bean
    public Klass klass() {
        Klass klass = new Klass();
        klass.setStudents(List.of(this.student01(), this.student02()));
        return klass;
    }

    @Bean
    public School school() {
        return new School(this.klass());
    }
}
