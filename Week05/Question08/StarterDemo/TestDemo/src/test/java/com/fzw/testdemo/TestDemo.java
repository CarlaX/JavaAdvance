package com.fzw.testdemo;

import com.fzw.mystarter.config.StarterDemoConfig;
import com.fzw.mystarter.pojo.School;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
@SpringBootTest
public class TestDemo {

    @Autowired
    private StarterDemoConfig starterDemoConfig;

    @Autowired
    private School school;

    @Test
    public void test() {
        System.out.println(starterDemoConfig);
        System.out.println(school);
    }
}
