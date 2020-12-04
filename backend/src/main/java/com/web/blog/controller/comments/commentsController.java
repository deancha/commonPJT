package com.web.blog.controller.comments;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.google.common.base.Equivalence.Wrapper;
import com.web.blog.dao.comment.CommentsDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.dto.comments.comment;
import com.web.blog.dto.comments.commentanduserWrapperClass;
import com.web.blog.dto.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin(origins = { "*" })
public class commentsController {

    @Autowired
    CommentsDao commentsDao;

    @Autowired
    UserDao userDao;
    private String success = "success";
    private String fail = "fail";

    @ApiOperation(value = "시간순으로 정렬된 & 포스트 아이디가 맞는 리스트 반환 ==>")
    @GetMapping(value = "/comments/{postid}")
    public Object getallcommentsListCorrectpostid(@PathVariable int postid) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();

        List<commentanduserWrapperClass> wrapperlist = new LinkedList<>();

        // poistid에 매핑되는 전체 댓글을 가져옴
        List<comment> commentResult = commentsDao.findCommentByPostid(postid);

        // 리스트를 돌면서 Useremail과 매핑되는 user를 가져온 후 profileimg만 빼옴 -> 그걸 wrapperclass에 넣음
        for (int i = 0; i < commentResult.size(); i++) {
            comment cur = commentResult.get(i);
            User user = userDao.getUserByUseremail(cur.getUseremail());
            commentanduserWrapperClass commentanduserWrapperclass = new commentanduserWrapperClass();
            commentanduserWrapperclass.setComment(cur);
            commentanduserWrapperclass.setProfileimg(user.getProfileimg());
            wrapperlist.add(commentanduserWrapperclass);
        }

        if (wrapperlist != null && !wrapperlist.isEmpty()) {
            result.put("data", success);
            result.put("object", wrapperlist);
            result.put("message", "댓글리스트 로딩완료");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", fail);
            result.put("message", "댓글리스트 로딩중 문제발생");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @ApiOperation(value = "댓글 추가")
    @PostMapping(value = "/comments")
    public Object insertcomment(@RequestBody comment nowcomment) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        nowcomment.setCreatedat(LocalDateTime.now());
        comment resultComment = commentsDao.save(nowcomment);
        if (resultComment != null) {
            result.put("data", success);
            result.put("object", resultComment);
            result.put("message", "댓글 입력완료");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", fail);
            result.put("message", "댓글 입력중 에러발생");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @ApiOperation(value = "댓글 수정")
    @PutMapping(value = "/comments")
    public Object updatecomment(@RequestBody comment nowcomment) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        Optional<comment> resultComment = commentsDao.findCommentByCommentsid(nowcomment.getCommentsid());
        if (resultComment.isPresent()) {
            nowcomment.setUpdatedat(LocalDateTime.now());
            commentsDao.save(nowcomment);
            result.put("data", success);
            result.put("object", resultComment);
            result.put("message", "댓글 수정완료");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", fail);
            result.put("message", "댓글 업데이트중 에러발생");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping(value = "/comments")
    public Object deletecomment(@RequestParam int commentsid) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        Optional<comment> resultComment = commentsDao.findCommentByCommentsid(commentsid);
        if (resultComment.isPresent()) {
            commentsDao.delete(commentsDao.findByCommentsid(commentsid));
            result.put("data", success);
            result.put("message", "댓글 삭제완료");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", fail);
            result.put("message", "댓글 삭제중 에러발생");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

}