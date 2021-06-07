package com.fzw.beandemo.demo04;

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
public class Demo04 {
    @Autowired
    private Person person;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);
        Demo04 demo04 = context.getBean(Demo04.class);
        System.out.println(demo04.person);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Demo04{" +
                "person=" + person +
                '}';
    }
}
