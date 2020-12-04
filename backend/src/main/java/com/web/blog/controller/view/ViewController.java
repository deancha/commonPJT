package com.web.blog.controller.view;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.web.blog.dao.post.PostDao;
import com.web.blog.dao.view.Unloginviewdao;
import com.web.blog.dao.view.Viewdao;
import com.web.blog.dto.post.Post;
import com.web.blog.dto.views.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin({ "* " })
public class ViewController {

    @Autowired
    Viewdao viewdao;

    @Autowired
    PostDao postdao;

    @Autowired
    Unloginviewdao unloginviewdao;

    @PostMapping(value = "/view")
    @ApiOperation(value = "조회 객체 등록")
    public Object insertview(@RequestBody view view) {

        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        view.setViewdate(LocalDateTime.now().plusHours(9));
        view insertedview = viewdao.save(view);
        if (insertedview == null) {
            result.put("data", "fail");
            result.put("message", "조회객체 등록실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {

            result.put("data", "success");
            result.put("message", "조회객체 등록성공");
            result.put("object", insertedview);
            response = new ResponseEntity<>(result, HttpStatus.OK);

        }
        return response;
    }

    @GetMapping(value = "/view/{postid}")
    @ApiOperation(value = "postid의 조회수 반환")
    public Object countview(@PathVariable(required = true) int postid) {

        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();

        int totalviewcount = viewdao.CountBypostid(postid);
        totalviewcount += unloginviewdao.findUnloginviewByPostid(postid).getViews();
        result.put("data", "success");
        result.put("message", "조회수 조회 성공");
        result.put("object", totalviewcount);
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping(value = "/getRecentView")
    @ApiOperation(value = "사용자가 방금 조회한 post리스트 반환")
    public Object getRecentView(@RequestBody view view) {

        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        List<Integer> postidlist = viewdao.findPostidByUseremailGroupByPostidOrderByViewdateDesc(view.getUseremail());
        
        if (postidlist == null || postidlist.isEmpty()) {
            result.put("data", "fail");
            result.put("message", "사용자가 조회한 게시글이 아직없음");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            ArrayList<Post> postlist = new ArrayList<>();
            for (Integer postid : postidlist) {
                postlist.add(postdao.findOne(postid));
            }
            if (postlist == null || postlist.isEmpty()) {
                result.put("data", "fail");
                result.put("message", "게시글 목록 불러오다가 에러");
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.put("data", "success");
                result.put("message", "조회리스트 로딩 성공");
                result.put("object", postlist);
                response = new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
        return response;
    }

}