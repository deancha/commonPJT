package com.web.blog.controller.visitor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.websocket.server.PathParam;

import com.web.blog.dao.post.PostDao;
import com.web.blog.dao.view.Unloginviewdao;
import com.web.blog.dao.view.Viewdao;
import com.web.blog.dao.visitor.VisitorDao;
import com.web.blog.dto.post.Post;
import com.web.blog.dto.views.unloginview;
import com.web.blog.dto.views.view;
import com.web.blog.dto.visitor.visitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin({ "* " })
public class VisitorController {


    @Autowired
    VisitorDao visitordao;

    
    @PutMapping(value = "/visitor")
    @ApiOperation(value = "cookie가 없으면  방문자수 1증가 해줌")
    public Object visitorincre() {

        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        visitor curvisitor = visitordao.findVisitorByDate(LocalDate.now());
        
        if (curvisitor == null) {
            visitor newvisitor = new visitor();
            newvisitor.setDate(LocalDate.now());
            newvisitor.setVisitors(1);
            visitordao.save(newvisitor);
            result.put("message", "날짜별 방문자수 새로 생성");
            result.put("object", newvisitor.getVisitors());
        } else {
            //성공시 
            int visitorsPerDay = curvisitor.getVisitors();
            curvisitor.setVisitors(visitorsPerDay + 1);
            visitordao.save(curvisitor);
            result.put("message", "방문자수 증가 성공");
            result.put("object", curvisitor.getVisitors());
            
        }
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @GetMapping(value = "/visitor")
    @ApiOperation(value = "쿠키가 있으면 방문자 수만 반환 ")
    public Object visitorupdate() {

        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        visitor curvisitor = visitordao.findVisitorByDate(LocalDate.now());
        
        result.put("message", "날짜별 방문자수 반환(cookie있을시)");
        if (curvisitor == null) {
            visitor newvisitor = new visitor();
            newvisitor.setDate(LocalDate.now());
            newvisitor.setVisitors(1);
            visitordao.save(newvisitor);
            result.put("object", newvisitor.getVisitors());
        } else {
            result.put("object", curvisitor.getVisitors());
            
        }
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

}