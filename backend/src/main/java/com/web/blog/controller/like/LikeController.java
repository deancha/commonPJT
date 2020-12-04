package com.web.blog.controller.like;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.web.blog.dao.like.LikeDao;
import com.web.blog.dto.like.like;
import com.web.blog.dto.post.postidlistobject;
import com.web.blog.dto.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
public class LikeController {

    @Autowired
    LikeDao likedao;

    @GetMapping("/like/{postId}")
    @ApiOperation(value = "post_id 에 할당된 좋아요 수 반환")
    public Object CountByPostId(@PathVariable(value = "postId", required = true) int postId) {

        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        long res = -1;
        // post_id 기준 좋아요 수를 반환한다.
        res = likedao.countByPostid(postId);
        if (res >= 0) {
            result.put("message", "좋아요수 산출 완료");
            result.put("object", res);
            result.put("data", "success");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("message", "좋아요 수 산출 중 오류발생!");
            result.put("data", "success");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;

    }

    @PostMapping("/like")
    @ApiOperation(value = "post_id의 좋아요 클릭")
    public Object insertLike(@RequestBody like like) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        like likeresult = likedao.save(like);
        if (likeresult == null) {
            result.put("data", "success");
            result.put("message", "좋아요 클릭시 오류 발생");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            result.put("data", "success");
            result.put("object", likeresult);
            result.put("message", "좋아요 클릭 완료");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @Transactional
    @DeleteMapping("/like")
    @ApiOperation(value = "좋아요 취소")
    public Object deletelike(@RequestParam String useremail, @RequestParam int postid) {
        ResponseEntity response = null;

        HashMap<String, Object> result = new HashMap<>();
        Optional<like> isClick = likedao.findLikeByUseremailAndPostid(useremail, postid);
        if (!isClick.isPresent()) {
            result.put("data", "fail");
            result.put("object", false);
            result.put("message", "좋아요 취소 오류 발생 없는데 삭제하는 경우");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            // 삭제
            result.put("data", "success");
            likedao.deleteByUseremailAndPostid(useremail, postid);
            result.put("object", true);
            result.put("message", "좋아요 취소 완료");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @PutMapping("/like")
    @ApiOperation(value = "사용자가 post_id를 눌렀는지를 반환")
    public Object asklike(@RequestBody like like) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        Optional<like> isClick = likedao.findLikeByUseremailAndPostid(like.getUseremail(), like.getPostid());
        if (!isClick.isPresent()) {
            result.put("data", "success");
            result.put("object", false);
            result.put("message", like.getUseremail() + " 이" + like.getPostid() + " 번의" + " 좋아요를 누르지 않음");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "success");
            result.put("object", true);
            result.put("message", like.getUseremail() + " 이" + like.getPostid() + " 번의" + "좋아요를 누름");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

}