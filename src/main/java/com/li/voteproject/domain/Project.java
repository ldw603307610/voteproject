package com.li.voteproject.domain;


import org.springframework.stereotype.Component;

import javax.persistence.*;

@Table(name = "project")
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer project_id;
    private String projectname;
    private Integer userid;
    private int round;//int的默认值是0，这个比较好用。

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    @Override
    public String toString() {
        return "Project{" +
                "project_id=" + project_id +
                ", projectname='" + projectname + '\'' +
                '}';
    }
}
