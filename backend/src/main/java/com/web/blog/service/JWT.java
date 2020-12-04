package com.web.blog.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWT {
    public static void main(String[] args) {
        String secrete_key = RandomStringUtils.randomAlphanumeric(256);

        byte[] SecreteKey = Base64.getDecoder().decode(secrete_key);

        Instant now = Instant.now();
        HashMap<String, Object> userinfoMap = new HashMap<>();
        userinfoMap.put("name", "sangwon");
        userinfoMap.put("age", "27");
        userinfoMap.put("email", "a@naver.com");
        userinfoMap.put("gender", "M");
        // add claim 으로 내가 보낼 user 정보를 포함하자
        String token = Jwts.builder().setSubject("SSAFY").setAudience("PJT TEST").addClaims(userinfoMap)
                .setIssuedAt(Date.from(now)).setExpiration(Date.from(now.plus(1, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(SecreteKey)).compact();

        System.out.println(token + " 를 해석하면 아랫것이 됩니다!");

        Jws<Claims> result = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(SecreteKey)).parseClaimsJws(token);

        System.out.println(result.getBody().get("name"));

    }
}