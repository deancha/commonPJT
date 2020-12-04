package com.web.blog.controller.account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = { "*" })
public class NaverLoginController {

    // Api id loading -> 서상원이름으로 얻어온거임!!

    private String CLIENT_ID = "FWG8CAXasb4QFDGecj7m";
    private String CLI_SECRET = "4IuJl0g1dd";

    // CallbackUrl에 메인페이지를 넣어두자
    // private String CallbackUrl = "http://localhost:8080/naver/callback";
    private String CallbackUrl = "http://i3a610.p.ssafy.io/api/naver/callback";

    // mainpage loading시에 네이버 api를 불러서 key와 결합하여 로그인 url를 생성하고 버튼 a 태그에 삽입한다.
    @RequestMapping("/naver")
    public Object testNaver(HttpSession session, Model model)
            throws UnsupportedEncodingException, UnknownHostException {
        HashMap<String, Object> result = new HashMap<>();
        ResponseEntity response = null;
        String redirectURI = URLEncoder.encode(CallbackUrl, "UTF-8");
        SecureRandom random = new SecureRandom();
        // 난수 발생c
        String state = new BigInteger(130, random).toString();
        String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
        apiURL += String.format("&client_id=%s&redirect_uri=%s&state=%s", CLIENT_ID, redirectURI, state);
        result.put("apiURL", apiURL);
        result.put("status", false);
        return response = new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 콜백 페이지 정의 -> 로그인이되고 나서 진행할 페이지를 mapping -> naver dev api 에서 로그인한 정보랑 일치해야지 작동
    @RequestMapping("/naver/callback")
    public void naverCallback(@RequestParam String code, @RequestParam String state, HttpServletResponse resp)
            throws IOException, ParseException {
        String redirectURI = URLEncoder.encode(CallbackUrl, "UTF-8");
        String apiURL;

        apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
        apiURL += "client_id=" + CLIENT_ID;
        apiURL += "&client_secret=" + CLI_SECRET;
        apiURL += "&redirect_uri=" + redirectURI;
        apiURL += "&code=" + code;
        apiURL += "&state=" + state;
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        String res = requestToServer(apiURL);
        String access_token ="";
        if (res != null && !res.equals("")) {
            Map<String, Object> parsedJson = new JSONParser(res).parseObject();
            access_token = (String) parsedJson.get("access_token");
            resp.sendRedirect("http://localhost:3000/#/user/social?type=naver&access_token=" + access_token);
        } else {
            resp.sendRedirect("http://localhost:3000/#/user/social");
        }
    }

    // 저장된 토큰 초기화 -> api call시에 자동로그인을 해제할 수있다.
    @RequestMapping("/naver/refreshToken")
    public Object refreshToken(String refreshToken) throws IOException, ParseException {
        String apiURL;
        apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=refresh_token&";
        apiURL += "client_id=" + CLIENT_ID;
        apiURL += "&client_secret=" + CLI_SECRET;
        apiURL += "&refresh_token=" + refreshToken;
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        String res = requestToServer(apiURL);
        result.put("currentuser", res);
        return response = new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping("/naver/deleteToken")
    public Object deleteToken(String accessToken) throws IOException {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        String apiURL;
        apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=delete&";
        apiURL += "client_id=" + CLIENT_ID;
        apiURL += "&client_secret=" + CLI_SECRET;
        apiURL += "&access_token=" + accessToken;
        apiURL += "&service_provider=NAVER";

        String res = requestToServer(apiURL);
        result.put("res", res);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @ResponseBody
    @RequestMapping("/naver/getProfile")
    public String getProfileFromNaver(String access_token) throws IOException {
        // 네이버 로그인 접근 토큰
        String apiURL = "https://openapi.naver.com/v1/nid/me";
        String headerStr = "Bearer " + access_token; // Bearer 다음에 공백 추가
        String res = requestToServer(apiURL, headerStr);
        return res;
    }

    @RequestMapping("/naver/invalidate")
    public String invalidateSession(HttpSession session) {
        session.invalidate();
        return "redirect:/naver";
    }

    // api 를 통신하는 메소드
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