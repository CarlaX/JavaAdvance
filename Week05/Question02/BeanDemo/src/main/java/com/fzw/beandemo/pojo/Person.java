package com.fzw.beandemo.pojo;

import java.util.Objects;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
public class Person {
    private String username;
    private String nickname;

    public Person() {
    }

    public Person(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Person person = (Person) o;
        return Objects.equals(username, person.username) && Objects.equals(nickname, person.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, nickname);
    }

    @Override
    public String toString() {
        return "Person{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
