package com.li.voteproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class IMailServiceImpl implements IMailService {
    Integer num;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Autowired
    private JavaMailSender javaMailSender;//springboot自带的邮箱发送接口
    @Value("${spring.mail.from}")
    private String from;
    @Override
    public void sendSimpleMail(String to, String subject, Integer content) {
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();//创建发送对象
        simpleMailMessage.setFrom(from);//发送方
        simpleMailMessage.setTo(to);//接受方
        simpleMailMessage.setSubject(subject);//主题
        simpleMailMessage.setText(String.valueOf(content));//内容
        javaMailSender.send(simpleMailMessage);//用接口发出去
    }
}
