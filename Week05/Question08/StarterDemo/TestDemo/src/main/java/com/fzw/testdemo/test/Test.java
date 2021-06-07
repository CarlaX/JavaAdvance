package com.fzw.testdemo.test;

import com.fzw.mystarter.config.StarterDemoConfig;
import com.fzw.mystarter.pojo.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
@Component
public class Test {
    @Autowired
    private StarterDemoConfig starterDemoConfig;

    @Autowired
    private School school;

    @PostConstruct
    public void postProcess() {
        System.out.println(starterDemoConfig);
        System.out.println(school);
    }
}
