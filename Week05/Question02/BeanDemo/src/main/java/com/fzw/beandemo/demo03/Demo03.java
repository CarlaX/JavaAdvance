package com.fzw.beandemo.demo03;

import com.fzw.beandemo.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
@Component
public class Demo03 {
    @Autowired
    private Person person;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.fzw.beandemo.demo03");

        Demo03 demo03_2 = context.getBean(Demo03.class);

        System.out.println(demo03_2.person);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Demo03{" +
                "person=" + person +
                '}';
    }
}
