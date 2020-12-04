package com.web.blog.controller.posttemp;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.web.blog.dao.posttemp.posttempDao;
import com.web.blog.dto.posttemp.posttemp;
import com.web.blog.dto.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@CrossOrigin(origins = { "*" })
public class posttempController {

    @Autowired
    posttempDao posttempdao;

    @GetMapping("/posttemp/{useremail}")
    @ApiOperation(value = "사실상 한 아이디의 이메일로 접근하는 경우밖에 없다 -> 모든 게시글을 볼 경우가 존재하지 않음")
    public Object selectAlldataByUseremail(@PathVariable String useremail) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        List<posttemp> list = null;
        list = posttempdao.findByUseremail(useremail);

        if (list != null) {
            result.put("data", "success");
            result.put("object", list);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "fail");
            result.put("object", list);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @DeleteMapping("/posttemp")
    @Transactional
    @ApiOperation(value = "임시저장 리스트중하나선택해서 삭제하는 경우")
    public Object deleteposttemp(@RequestParam int postid) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        posttempdao.deleteByPostid(postid);
        Optional<posttemp> tposttemp = posttempdao.findByPostid(postid);
        if (tposttemp.isPresent()) {
            result.put("data", "fail");
            result.put("message", "삭제실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "success");
            result.put("message", "삭제 성공");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;

    }

    @GetMapping("/posttemp/topost/{postid}")
    @ApiOperation(value = "임시저장 리스트중하나선택해서 가져오는 경우 ")
    public Object selectOneposttemp(@PathVariable int postid) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        Optional<posttemp> posttemp = posttempdao.findByPostid(postid);
        if (posttemp.isPresent()) {
            // 있으니까 가져올수 있다.
            result.put("data", "success");
            result.put("object", posttemp);
            result.put("message", "로딩 성공 ");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "fail");
            result.put("message", "로딩 실패 => 없는 번호를 조회하려함");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;

    }

    @PutMapping("/posttemp")
    @ApiOperation(value = "임시저장 리스트중하나선택해서 수정하는 경우")
    public Object putposttemp(@RequestBody posttemp nowposttemp) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        Optional<posttemp> tposttemp = posttempdao.findByPostid(nowposttemp.getPostid());
        if (tposttemp.isPresent()) {
            // 있으므로 그냥 덮어씌우면 된다.

            nowposttemp.setUpdatedat(LocalDateTime.now());
            posttempdao.save(nowposttemp);
            result.put("data", "success");
            result.put("message", "수정 성공");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "fail");
            result.put("message", "수정 실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;

    }

    @PostMapping("/posttemp")
    @ApiOperation(value = "임시저장에 입력 하는 경우")
    public Object postposttemp(@RequestBody posttemp nowposttemp) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        // 있으므로 그냥 덮어씌우면 된다.
        nowposttemp.setCreatedat(LocalDateTime.now());
        posttemp res = posttempdao.save(nowposttemp);
        if (res == null) {
            result.put("data", "fail");
            result.put("message", "저장 실패 ");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "success");
            result.put("object", res);
            result.put("message", "저장 성공 ");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        result.put("data", "success");

        return response;

    }
}