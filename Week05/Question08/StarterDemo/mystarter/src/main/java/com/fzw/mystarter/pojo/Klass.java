package com.fzw.mystarter.pojo;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
public class Klass {

    @Autowired
    private List<Student> students;

    public void dong() {
        System.out.println(this.getStudents());
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Klass{" +
                "students=" + students +
                '}';
    }
}
