package com.li.voteproject.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.li.voteproject.IMailService;
import com.li.voteproject.IMailServiceImpl;
import com.li.voteproject.Service;
import com.li.voteproject.VoteTime;
import com.li.voteproject.dao.ChoiceDao;
import com.li.voteproject.dao.JudgeDao;
import com.li.voteproject.dao.ProjectDao;
import com.li.voteproject.domain.Choice;
import com.li.voteproject.domain.Project;
import com.li.voteproject.domain.User;
import com.li.voteproject.dao.UserDao;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static java.lang.Thread.sleep;

@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    Service service;
    @RequestMapping("/user/index")//与index里面的方法相呼应，index里面点了登录，就跳转页面并调用login方法
    public String login(@RequestParam("username") String username, @RequestParam("password") String password , Model model, HttpSession session){
        if (service.isExist(username,password,session)){
            model.addAttribute("un","您的用户名为："+username);
            return "manage";
        }else {
            model.addAttribute("msg","用户名或密码不正确");
            return "index";
        }
    }
    @RequestMapping("/user/jumptoregister")
    public String jumptoregister(){
        return "register";
    }
    @Autowired
    UserDao userDao;
    @RequestMapping("/user/register")
    public String register(@RequestParam("newusername") String newusername,@RequestParam("newpassword") String newpassword ,Model model){
        if (!StringUtils.isEmptyOrWhitespaceOnly(newusername)&&!StringUtils.isEmptyOrWhitespaceOnly(newpassword)){
            User user=new User();
            user.setPassword(newpassword);
            user.setUsername(newusername);
            user.setUser_id(null);
            userDao.save(user);
            return "index";
        }
            model.addAttribute("wrong","输入的用户名或密码不能为空");
            return "register";

    }
    @RequestMapping("/user/jumpbacktoindex")
    public String jumpbacktoindex(){
        return "index";
    }
    @RequestMapping("/user/jumptocreate")
    public String jumptocreate(){return "create";}
    @RequestMapping("/user/createprojectname")
    public String projectcreate(@RequestParam("projectname")String projectname ,Model model,HttpSession session,@RequestParam("round")Integer round){
        User sess = (User) session.getAttribute("user");
        if(sess == null){
            return "index";
        }
        Integer num=sess.getUser_id();
        if (service.isExist03(num)){//如果已存在我就不存了
            model.addAttribute("msg","对不起，一个用户只能持有一个项目。");
            return "projectcreate";
        }else {
            service.create(projectname,round);//就是这里,将项目名字和轮数输入进去
            Project project = projectDao.findByProjectname(projectname);
            project.setRound(round);
            session.setAttribute("project",project);
            User user03=new User();
            Project project1=new Project();
            user03= (User) session.getAttribute("user");
            Integer num03=user03.getUser_id();
            project1= (Project) session.getAttribute("project");
            Integer num04=project1.getProject_id();
            projectDao.updateuseridByproject_id(num03,num04);//根据project_id来更新那一行的userid。
            model.addAttribute("msg02","恭喜您注册成功");
            return "projectcreate";

        }
    }
    @RequestMapping("/user/createchoice")
    public String createchoice(@RequestParam("choicename")String choicename,Model model){
        if (!StringUtils.isEmptyOrWhitespaceOnly(choicename)){
            service.choicecreate(choicename);
            model.addAttribute("msg","恭喜您创建成功");
            return "choicecreate";
        }
        model.addAttribute("msg02","输入不能为空，请重试");
        return "choicecreate";
    }
    @RequestMapping("/user/judgecreate")
    public String judgecreate(@RequestParam("judgename")String judgename,@RequestParam("judgepassword")String judgepassword ,Model model){
        if (!StringUtils.isEmptyOrWhitespaceOnly(judgename)&&!StringUtils.isEmptyOrWhitespaceOnly(judgepassword)){
            service.judgecreate(judgename,judgepassword);
            model.addAttribute("msg","恭喜您创建成功");
            return "judgecreate";
        }
        model.addAttribute("msg02","用户名和密码不能为空");
        return "judgecreate";
    }
    @RequestMapping("/user/jumptojudgelogin")
    public String jumptojudgelogin(HttpSession session,Model model){
        Project project= (Project) session.getAttribute("project");
        int num=project.getProject_id();
        String name=project.getProjectname();
        model.addAttribute("msg","项目的id是："+num+"，名称为："+name);
        return "judgelogin";
    }
    @Autowired
    ChoiceDao choiceDao;
    @Autowired
    VoteTime voteTime;//把计数器类持久化了防止刷票
    @RequestMapping("/user/judgelogin")
    public String judgelogin(@RequestParam("judgeusername")String judgeusername,@RequestParam("judgepassword")String judgepassword,Model model,@RequestParam("projectid")int projectid,HttpSession session){
        Project project= (Project) session.getAttribute("project");
        int num=project.getProject_id();
        voteTime.setTime(0);//每次评委登录的时候，计数器都会自动设置为0
        if (service.isExist02(judgeusername,judgepassword)&&num==projectid){
            List<String> a =choiceDao.findAllByChoicename();
            List<Integer> b=choiceDao.findAllByChoice_idList();
            List<Integer> c=choiceDao.findAllByChoicenumble();
            model.addAttribute("cmg","选项信息有："+"选项的id为："+b+",选项的内容是"+a+",现在的投票数目有："+c);
            return "vote";
        }
        model.addAttribute("wrongmess","输入错误，请重新输入");
        return "judgelogin";
    }
    @RequestMapping("/user/vote")
    public String vote(Model model,@RequestParam("id")Integer id){
        int num=choiceDao.findByChoicenumble(id);
        num++;
        int times=voteTime.getTime();
        times++;
        voteTime.setTime(times);//每次投票都会给计数器加1，如果不等于1就不能投票
        if (voteTime.getTime()!=1){
            model.addAttribute("wrongmessage","您已经投过票或者已经弃票，请不要再投票了");
            return "vote";
        }
        choiceDao.updatechoicenumbleBychoice_id(id,num);//增加票数
        List<Integer> c=choiceDao.findAllByChoicenumble();
        List<Integer> b=choiceDao.findAllByChoice_idList();
        model.addAttribute("msg","恭喜投票成功"+",项目的id为："+b+"现在的票数有："+c);
        return "voteresult";
    }
    @RequestMapping("/user/drop")
    public String drop(Model model){
        int time=voteTime.getTime();
        time=time+10;
        voteTime.setTime(time);
        model.addAttribute("drop","弃票成功");
        return "vote";
    }
    @RequestMapping("/user/back")
    public String backtofirst(){return "control";}
    @RequestMapping("/user/jumptocreate01")
    public String jump01(){return "projectcreate";}
    @RequestMapping("/user/jumptocreate02")
    public String jump02(){return "choicecreate";}
    @RequestMapping("/user/jumptocreate03")
    public String jump03(){return "judgecreate";}
    @RequestMapping("/user/order")
    public String order(Model model,HttpSession session){
       Integer choice_id=choiceDao.findOneByChoice_id();
       choiceDao.deleteByChoice_id(choice_id);//删除掉最后一个。
       List<String> a=choiceDao.findAllByChoicename();
       List<Integer> b=choiceDao.findAllByChoicenumble();
       if(a == null){
            model.addAttribute("wrong02","对不起，您还没有创建选项");
            return "control";
        }
       Project rp= (Project) session.getAttribute("project");
       Integer project_id=rp.getProject_id();
       int round=rp.getRound();
       round--;
       projectDao.updateroundByproject_id(round,project_id);
       Project project=new Project();
       project.setRound(round);//把减少过的round存进去。
        project.setProject_id(project_id);
        project.setProjectname(rp.getProjectname());//重置了session，要把信息都重新存回去。
       session.setAttribute("project",project);
       if(round > 1){//正常情况下显示这个。
            model.addAttribute("msg","本轮选出的选项是："+a+",它们的票数是："+b+",现在是倒数第"+round+"轮。");
            return "final";
       }
       else if(round == 1){
           String c=choiceDao.findAllByChoicename02();//倒序查出最高的那个。
           int d=choiceDao.findAllByChoicenumble02();
           model.addAttribute("final","最终结果是："+c+"，它最终得票为："+d+"，谢谢参与~");
           return "final";
       }
       model.addAttribute("wrong","请至少设置一轮投票谢谢。");
       return "final";
    }
    @Autowired
    ProjectDao projectDao;
    @Autowired
    JudgeDao judgeDao;
    @RequestMapping("/user/delete")
    public String delete(HttpSession session){
    projectDao.deleteAllInBatch();
    judgeDao.deleteAllInBatch();
    choiceDao.deleteAllInBatch();
    session.invalidate();//清除掉session的缓存
    return "index";
    }
    @RequestMapping("/user/jumptocontrol")
    public String jumptocontrol(HttpSession session,Model model){
        Project project= (Project) session.getAttribute("project");
        if(project != null){
            String a=project.getProjectname();
            Integer b=project.getProject_id();
            int c=project.getRound();
            model.addAttribute("msg","项目的id为："+b+"，项目名为："+a+",此项目有"+c+"轮。");
            return "control";
        }
        model.addAttribute("wrong","对不起，您还没有创建好项目，无法访问！");
        return "manage";
    }
    @RequestMapping("/user/check")
    public String check(Model model,HttpSession session,@RequestParam("content")Integer content){
        IMailServiceImpl mailService02=new IMailServiceImpl();
        mailService02= (IMailServiceImpl) session.getAttribute("mailkey");//取出发送邮件时存下的对象
        if (content.equals(mailService02.getNum())){
            return "reset";
        }
        model.addAttribute("msg","输入错误");//不正确的话返回什么。
        return "check";
    }
    @RequestMapping("/user/newpassword")
    public String updatepw(HttpSession session, @RequestParam("newpassword")Integer newpassword){
        User user04=new User();
        user04= (User) session.getAttribute("user");
        Integer num=user04.getUser_id();
        userDao.updatepassword(newpassword,num);
        return "index";
    }
    @RequestMapping("/user/backtomanage")
    public String back(){return "manage";}
    @RequestMapping("/user/tocodecheck")
    public String tocodecheck(){return "codecheck";}
    @Autowired
    DefaultKaptcha defaultKaptcha;
    @RequestMapping("/user/kaptcha")//这个请求是生成一个验证码
    public void kaptcha(HttpSession session,HttpServletResponse response){//生成验证码的方法，用到了http的response。
        String createText=defaultKaptcha.createText();
        BufferedImage bufferedImage =defaultKaptcha.createImage(createText);//以上两行用于生成验证码
        session.setAttribute("right",createText);//将正确的验证码存在session里面。
        response.setContentType("bufferedImage/png");
        try {
            OutputStream os=response.getOutputStream();//首次用到串流和IO流。
            ImageIO.write(bufferedImage,"png",os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Autowired
    private IMailService iMailService;
    @Value("${spring.mail.from}")
    private String from;
    @RequestMapping("/user/sendmail")
    public String sendmail(Model model,HttpSession session,@RequestParam("codecheck")String codecheck){//验证之前的验证码，并且发邮件
        String right= (String) session.getAttribute("right");
        if(codecheck.equals(right)){
            IMailServiceImpl mailService=new IMailServiceImpl();
            mailService.setNum((int) (Math.random()*10000));//随机的验证码，充当发送的内容
            session.setAttribute("mailkey",mailService);//存下这个对象，后面好调用，用来判断验证码是否正确。
            session.setMaxInactiveInterval(5*60);//设置有效时间为5分钟
            iMailService.sendSimpleMail(from,"检验功能",mailService.getNum());//发送对象、主题、内容
            model.addAttribute("msgs","恭喜邮件发送成功,请填写验证码");
            return "check";
        }
        model.addAttribute("wrong","输入错误，请重新输入");
        return "codecheck";
    }
    @RequestMapping("/user/backtolast")
    public String backtolast(){return "manage";}
}

