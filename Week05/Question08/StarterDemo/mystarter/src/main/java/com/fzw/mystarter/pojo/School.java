package com.fzw.mystarter.pojo;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
public class School {

    @Autowired
    private Klass class1;

    public School() {
    }

    public School(Klass klass) {
        this.class1 = klass;
    }

    public void ding() {
        System.out.println("Class1 have " + this.class1.getStudents().size());
    }

    public Klass getClass1() {
        return class1;
    }

    public void setClass1(Klass class1) {
        this.class1 = class1;
    }

    @Override
    public String toString() {
        return "School{" +
                "class1=" + class1 +
                '}';
    }
}
