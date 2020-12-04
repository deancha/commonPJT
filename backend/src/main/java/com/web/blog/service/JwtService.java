package com.web.blog.service;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import com.web.blog.dto.user.User;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtService {

    private String salt = RandomStringUtils.randomAlphanumeric(256);
    private int expireMin = 5;

    public String CreateJwt(User user) {
        Instant now = Instant.now();
        return Jwts.builder().setHeaderParam("typ", "JWT").setSubject("SSAFY Login Token").setAudience("PJT TEST")
                .claim("User", user).setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expireMin, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, salt.getBytes()).compact();
    }

    public Map<String, Object> GetUserInfo(String JWT) {
        Jws<Claims> result = null;
        try {
            result = Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(JWT);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return result.getBody();
    }

    public void CheckJWT(String jwt) {

        Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(jwt);

    }
}