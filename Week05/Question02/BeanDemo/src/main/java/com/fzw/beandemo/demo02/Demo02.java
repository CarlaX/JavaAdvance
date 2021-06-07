package com.fzw.beandemo.demo02;

import com.fzw.beandemo.pojo.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
public class Demo02 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.fzw.beandemo.demo02");
        Person person = context.getBean(Person.class);
        System.out.println(person);
    }
}
