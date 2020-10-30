package com.li.voteproject.domain;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "judge")
public class Judge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer judge_id;
    private String username;
    private String password;

    public Integer getJudge_id() {
        return judge_id;
    }

    public void setJudge_id(Integer judge_id) {
        this.judge_id = judge_id;
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
        return "Judge{" +
                "judge_id=" + judge_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
