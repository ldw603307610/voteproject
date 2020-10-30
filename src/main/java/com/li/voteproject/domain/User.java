package com.li.voteproject.domain;

import com.li.voteproject.domain.Project;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Table(name = "user")//这儿写表名就行了
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自动递增的策略
    private Integer user_id;
    private String username;
    private String password;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
