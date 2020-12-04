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

import javax.servlet.http.HttpServletResponse;

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
public class GithubLoginController {

    
    private final String CLIENT_ID = "ad1486460d6f6c2dc096";
    private final String CLIENT_SECRET = "7797b72f9c53ed95d0f41503670dd928e9d13d3b";
    private final String REDIRECT_URI = "http://i3a610.p.ssafy.io/api/github/callback";
    
    @RequestMapping("/github")
    public Object requestGithubLogin() throws UnsupportedEncodingException, UnknownHostException {

        HashMap<String, Object> result = new HashMap<>();
        ResponseEntity response = null;
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();

        String apiURL = "https://github.com/login/oauth/authorize?";
        // System.out.println(apiURL);
        apiURL += String.format("client_id=%s&redirect_uri=%s&state=%s&scope=%s", CLIENT_ID, REDIRECT_URI, state ,"user:email");

        // System.out.println(apiURL);
        result.put("apiURL", apiURL);
        result.put("status", false);
        return response = new ResponseEntity<>(result, HttpStatus.OK);
    }

    //access_token 반환 
    @RequestMapping("/github/callback")
    public void githubCallback(@RequestParam String code, @RequestParam String state, HttpServletResponse resp) throws IOException, ParseException {
        HashMap<String, Object> result = new HashMap<>();

        System.out.println("code : " + code);
        String apiURL = "https://github.com/login/oauth/access_token?";
        apiURL += "client_id=" + CLIENT_ID;
        apiURL += "&client_secret=" + CLIENT_SECRET;
        apiURL += "&code=" + code;
        apiURL += "&redirect_uri=" + REDIRECT_URI;
        apiURL += "&state=" + state;
        // System.out.println("url : " + apiURL);
        String res = requestToServer(apiURL);

        System.out.println(res);
        if (res != null && !res.equals("")) {
            resp.sendRedirect("http://i3a610.p.ssafy.io/#/user/social?type=github&" + res);
        } else {
            resp.sendRedirect("http://i3a610.p.ssafy.io/#/user/social");
        }
    }

    @ResponseBody
    @RequestMapping("/github/getProfile")
    public String getProfileFromKakao(String access_token) throws Exception{
        String apiURL = "https://api.github.com/user";
        String header = "Token " + access_token;
        String res = requestToServer(apiURL, header);
        return res;
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