package com.web.blog.controller.account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin(origins = { "*" })
@Controller
public class KakaoLoginController {

    // http://localhost:8080/login/oauth2/code/

    // 카카오
    // restapi 키 : 75c8bc2e831437196ae654a4f306acd1
    // client secret 코드 : tipPpjTk3wjZGOqfCr7zpZG1dQ1QA0j8

    private final String REST_API_KEY = "75c8bc2e831437196ae654a4f306acd1";
    // private final String REDIRECT_URI = "http://localhost:8080/kakao/callback";
    private final String REDIRECT_URI = "http://i3a610.p.ssafy.io/api/kakao/callback";
    // private final String LOGOUT_REDIRECT_URI = "http://localhost:8080/kakao/logoutcallback";
    private final String LOGOUT_REDIRECT_URI = "http://i3a610.p.ssafy.io/api/kakao/logoutcallback";

    @RequestMapping("/kakao")
    public Object requestKakaoLogin() throws UnsupportedEncodingException, UnknownHostException {

        HashMap<String, Object> result = new HashMap<>();
        ResponseEntity response = null;
        String apiURL = "https://kauth.kakao.com/oauth/authorize?";
        apiURL += String.format("client_id=%s&redirect_uri=%s&response_type=code", REST_API_KEY, REDIRECT_URI);

        // System.out.println(apiURL);
        result.put("apiURL", apiURL);
        result.put("status", false);
        return response = new ResponseEntity<>(result, HttpStatus.OK);
    }

    // access_token 반환
    @RequestMapping("/kakao/callback")
    public void getTokenFromKakao(@RequestParam String code, HttpServletResponse resp)
            throws IOException, ParseException {
        HashMap<String, Object> result = new HashMap<>();

        String apiURL = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code";
        apiURL += "&client_id=" + REST_API_KEY;
        apiURL += "&redirect_uri=" + REDIRECT_URI;
        apiURL += "&code=" + code;

        String res = requestToServer(apiURL);
        String access_token = "";
        if (res != null && !res.equals("")) {
            Map<String, Object> parsedJson = new JSONParser(res).parseObject();
            access_token = (String) parsedJson.get("access_token");
            resp.sendRedirect("http://i3a610.p.ssafy.io/#/user/social?type=kakao&access_token=" + access_token);
        } else {
            resp.sendRedirect("http://i3a610.p.ssafy.io/#/user/social");
        }
    }

    @ResponseBody
    @RequestMapping("/kakao/getProfile")
    public String getProfileFromKakao(String access_token) throws Exception {
        String apiURL = "https://kapi.kakao.com/v2/user/me?";
        String header = "Bearer " + access_token;
        String res = requestToServer(apiURL, header);
        return res;
    }

    @RequestMapping("/kakao/logout")
    public void logoutFromKakao() throws IOException, ParseException {

        SecureRandom random = new SecureRandom();
        // 난수 발생c
        String state = new BigInteger(130, random).toString();

        String apiURL = "https://kauth.kakao.com/oauth/logout?";
        apiURL += "state=" + state;
        apiURL += "&client_id=" + REST_API_KEY;
        apiURL += "&logout_redirect_uri=" + LOGOUT_REDIRECT_URI;
        System.out.println(apiURL);
        String res = requestToServer(apiURL);
    }

    private String requestToServer(String apiURL) throws IOException {
        return requestToServer(apiURL, "");
    }

    private String requestToServer(String apiURL, String headerStr) throws IOException {
        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if (headerStr != null && !headerStr.equals("")) {
            con.setRequestProperty("Authorization", headerStr);
        }
        int responseCode = con.getResponseCode();
        BufferedReader br;
        System.out.println("서버 응답 코드=" + responseCode);
        if (responseCode == 200) { // 정상 호출
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else { // 에러 발생
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        String inputLine;
        StringBuffer res = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            res.append(inputLine);
        }
        br.close();
        if (responseCode == 200) {
            return res.toString();
        } else {
            return null;
        }
    }
}