package com.li.voteproject;

import com.li.voteproject.dao.ChoiceDao;
import com.li.voteproject.dao.JudgeDao;
import com.li.voteproject.dao.ProjectDao;
import com.li.voteproject.dao.UserDao;
import com.li.voteproject.domain.Choice;
import com.li.voteproject.domain.Judge;
import com.li.voteproject.domain.Project;
import com.li.voteproject.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {
    @Autowired
    UserDao userDao;
    public boolean isExist (String username, String password, HttpSession session){
        User user = userDao.findByUsernameAndPassword(username,password);
        if (user!=null){
            user.setPassword(null);//密码一定不能放进session，极其不安全！
            session.setAttribute("user",user);
        }
        return null != user;//这个只会返回一个布尔值，也就是有user返回true，没有就返回false。
    }

    @Autowired
    ProjectDao projectDao;
    public void create(String projectname,Integer round){
        Project project = new Project();
       project.setProjectname(projectname);
       project.setRound(round);
       project.setProject_id(null);
       projectDao.save(project);
    }
    @Autowired
    JudgeDao judgeDao;
    public void judgecreate(String username,String password){
        Judge judge = new Judge();
        judge.setUsername(username);
        judge.setPassword(password);
        judge.setJudge_id(null);
        judgeDao.save(judge);
    }
    @Autowired
    ChoiceDao choiceDao;
    public void choicecreate(String choicename){
        Choice choice = new Choice();
        choice.setChoicename(choicename);
        choice.setChoicenumble(0);
        choice.setChoice_id(null);
        choiceDao.save(choice);
    }
    public boolean isExist02(String username,String password){
        Judge judge = new Judge();
        judge = judgeDao.findByUsernameAndPassword(username,password);
        return null != judge;
    }
    public boolean isExist03(Integer num){
        Project project = new Project();
        project = projectDao.findByUserid(num);
        return null != project;//存在就返回true
    }
}
