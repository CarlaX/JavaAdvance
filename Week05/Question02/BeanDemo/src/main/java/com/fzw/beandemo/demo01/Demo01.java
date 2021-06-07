package com.fzw.beandemo.demo01;

import com.fzw.beandemo.pojo.Person;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
public class Demo01 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Person person = context.getBean(Person.class);
        System.out.println(person);
    }
}
