package com.web.blog.controller.account;

import java.util.HashMap;

import com.web.blog.dto.user.User;
import com.web.blog.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = { "*" })
public class SendMailController {

    @Autowired
    private NotificationService notificationservice;

    @PostMapping("/account/SendCheckEmail")
    @ApiOperation(value = "회원 인증메일")
    public Object signupSuccess(@RequestBody User user) {
        String Rnumber = null;
        System.out.println("이름 : " + user.getUsername() + " 이메일 주소 : " + user.getUseremail());
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        try {
            Rnumber = notificationservice.sendMail(user.getUseremail(), user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Rnumber != null) {
            result.put("data", "success");
            result.put("RandomNumber", Rnumber);
            result.put("name", user.getUsername());
            result.put("message", "Rnumber는 전송된 인증번호");
            result.put("status", true);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "fail");
            result.put("status", true);
            response = new ResponseEntity<>(result, HttpStatus.OK);

        }
        return response;
    }

}