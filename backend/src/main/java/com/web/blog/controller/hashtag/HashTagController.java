package com.web.blog.controller.hashtag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.web.blog.dao.hashtag.HashTagDao;
import com.web.blog.dao.post.PostDao;
import com.web.blog.dto.hashtag.hashtag;
import com.web.blog.dto.hashtag.hashtagwrapper;
import com.web.blog.dto.post.Post;
import com.web.blog.dto.post.postandlikeandcommentWrapperClass;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Delegate;

@CrossOrigin(origins = { "*" })
@RestController
public class HashTagController {

    @Autowired
    private HashTagDao hashtagDao;

    private final String success = "success";
    private final String fail = "fail";

    @PostMapping("/hashtag")
    @ApiOperation(value = "hashtag 추가 ")
    public Object insertHashTag(@RequestBody hashtag nowhashtag) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        nowhashtag.setWord(nowhashtag.getWord().toUpperCase());
        Optional<hashtag> isres = hashtagDao.findHashtagByPostidAndWord(nowhashtag.getPostid(), nowhashtag.getWord());

        if (isres.isPresent()) {
            // 저장중 오류발생
            result.put("message", "해쉬 태그 저장중 오류발생 / 이미 있음 ");
            result.put("data", fail);
            result.put("status", "failed");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            final hashtag reshashtag = hashtagDao.save(nowhashtag);
            result.put("message", "해쉬태그 저장완료");
            result.put("data", success);
            result.put("status", true);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @GetMapping("/hashtag/{postid}")
    @ApiOperation(value = "해당 postid의  hashtag리스트를 반환한다")
    public Object selecthashtaglistBypostid(@PathVariable final int postid) {
        ResponseEntity response = null;
        final HashMap<String, Object> result = new HashMap<>();
        final List<hashtag> hashTagList = hashtagDao.findHashtagByPostid(postid);
        if (hashTagList != null && !hashTagList.isEmpty()) {
            result.put("object", hashTagList);
            result.put("message", "해쉬 태그 리스트 저장완료");
            result.put("data", success);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("message", "해쉬 태그 리스트 저장중 오류발생 서상원을 불러주세요");
            result.put("data", fail);

            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @DeleteMapping("/hashtag")
    @ApiOperation(value = "hashtag 객체를 전달하여 해당 postid에 있는 word 해쉬태그 삭제")
    public Object deletehashtag(@RequestParam final int postid, @RequestParam final String word) {
        ResponseEntity response = null;
        final HashMap<String, Object> result = new HashMap<>();
        final Optional<hashtag> isPresent = hashtagDao.findHashtagByPostidAndWord(postid, word);
        if (isPresent.isPresent()) {
            // 있으면 삭제 가능
            hashtagDao.deleteByPostidAndWord(postid, word);
            result.put("message", "해쉬 태그 삭제완료 ");
            result.put("data", success);
            result.put("status", true);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("message", "해쉬 태그 삭제시 문제발생");
            result.put("data", fail);
            result.put("status", false);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;

    }

    @GetMapping("/gethashtagall")
    @ApiOperation(value = "전체 해시태그 및 빈도수 추출")
    public Object gethashtagall() {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        List<hashtag> total = hashtagDao.findAll();
        HashMap<String, Integer> cnt = new HashMap<>();
        HashMap<String, Boolean> chk = new HashMap<>();
        for (int i = 0; i < total.size(); i++) {
            if (cnt.containsKey(total.get(i).getWord().toUpperCase())) {
                cnt.put(total.get(i).getWord().toUpperCase(), cnt.get(total.get(i).getWord().toUpperCase()) + 1);
            } else {
                cnt.put(total.get(i).getWord().toUpperCase(), 1);
            }
        }
        List<hashtagwrapper> res = new ArrayList<>();
        for (int i = 0; i < total.size(); i++) {
            if (chk.containsKey(total.get(i).getWord().toUpperCase())) {
                continue;
            } else {
                hashtagwrapper t = new hashtagwrapper(total.get(i).getWord().toUpperCase(),
                        cnt.get(total.get(i).getWord().toUpperCase()));
                chk.put(total.get(i).getWord().toUpperCase(), true);
                res.add(t);
            }
        }
        Collections.sort(res, new Comparator<hashtagwrapper>() {
            @Override
            public int compare(hashtagwrapper o1, hashtagwrapper o2) {
                return o2.getCnt() - o1.getCnt();
            }
        });
        if (total != null) {
            // 있으면 삭제 가능

            result.put("message", "해쉬 태그 삭제완료 ");
            result.put("data", success);
            result.put("object", res);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("message", "해쉬 태그 삭제시 문제발생");
            result.put("data", fail);
            result.put("status", false);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;

    }

}
