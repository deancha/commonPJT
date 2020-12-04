package com.web.blog.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String Jwt = request.getHeader("auth_token");
        System.out.println(Jwt);
        if (Jwt != null && Jwt.length() > 0) {
            jwtService.CheckJWT(Jwt);
            return true;
        } else {
            throw new RuntimeException("인증 토큰이 없음!");
        }
    }
}