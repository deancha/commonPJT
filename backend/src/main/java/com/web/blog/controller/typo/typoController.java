package com.web.blog.controller.typo;

import java.util.HashMap;

import com.web.blog.dao.typo.typoDao;
import com.web.blog.dto.typo.typo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;

@RestController
@CrossOrigin({ "*" })
public class typoController {

    @Autowired
    typoDao typodao;

    @PostMapping("/typo")
    @ApiOperation(value = "단어추가 // 내부적으로 돌아가는 서비스")
    public Object inserttypo(@RequestBody typo typo) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        typo restypo = typodao.save(typo);
        if (restypo == null) {
            result.put("data", "fail");
            result.put("message", "저장중 실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "success");
            result.put("message", "저장 성공");
            result.put("object", typo);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }
}