package com.web.blog.controller.account;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class AccountExceptionController {

    @ExceptionHandler(value = { RuntimeException.class })
    public Map<String, Object> ServerErrorCatcher(Exception e) {
        Map<String, Object> res = new HashMap<>();
        res.put("data", "login please");
        res.put("msg", e.getMessage());
        return res;

    }
}