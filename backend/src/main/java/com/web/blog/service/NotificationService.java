package com.web.blog.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String sendMail(String email, String uid) throws MailException {
        // send mail
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom("SSAFY");
        mail.setSubject("안녕하세요 SSAFY 인증번호 안내입니다.");
        String RandomNumber = MakeRandomNumber();
        System.out.println(RandomNumber);
        StringBuilder sb = new StringBuilder();
        sb.append(uid + " 님의 인증번호는 " + RandomNumber + "  입니다!");
        sb.append("\n\n");
        sb.append("회원 가입의 인증번호란에 기입해주세요");

        sb.append("---SSAFY 3기 10조 PJT ---");
        mail.setText(sb.toString());

        javaMailSender.send(mail);
        return RandomNumber;
    }

    public String MakeRandomNumber() {
        String return_VAL = "";
        Random r = new Random();
        for (int i = 0; i < 6; i++) {
            int num = r.nextInt(10);
            return_VAL += Integer.toString(num);
        }
        return return_VAL;
    }
}