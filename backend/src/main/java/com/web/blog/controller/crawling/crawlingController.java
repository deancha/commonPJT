package com.web.blog.controller.crawling;

import java.util.HashMap;

import com.web.blog.service.Crawling.SeleniumCrawling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin({ "*" })
public class crawlingController {

    private SeleniumCrawling crawler;

    @GetMapping(value = "/crawling")
    @ApiOperation(value = "크롤러작동")
    public Object doCrawling() {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();

        crawler = new SeleniumCrawling();
        if (crawler != null) {
            try {
                result.put("data", "success");
                result.put("object", crawler.DoCrawling());
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                result.put("data", "fail");
                result.put("message", "크롤러 동작 중 예외 발생");
                return response = new ResponseEntity<>(result, HttpStatus.OK);
            }

        }
        return response;
    }
}