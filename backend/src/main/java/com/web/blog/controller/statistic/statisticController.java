package com.web.blog.controller.statistic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.web.blog.dao.like.LikeDao;
import com.web.blog.dao.post.PostDao;
import com.web.blog.dao.typo.typoDao;
import com.web.blog.dao.view.Viewdao;
import com.web.blog.dao.visitor.VisitorDao;
import com.web.blog.dto.post.Post;
import com.web.blog.dto.post.PostidandviewcntProjectionInterface;
import com.web.blog.dto.post.postandlikeandcommentWrapperClass;
import com.web.blog.dto.post.posttitleandviewcntwrapperclass;
import com.web.blog.dto.statistic.statiscPostLikeWrapperClass;
import com.web.blog.dto.statistic.statisticViewSenderWrapperClass;
import com.web.blog.dto.statistic.statisticviewWrapperClass;
import com.web.blog.dto.typo.typo;
import com.web.blog.dto.typo.typofrequencywrapperclass;
import com.web.blog.dto.visitor.visitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin({ "*" })
public class statisticController {

    @Autowired
    typoDao typodao;

    @Autowired
    PostDao postdao;

    @Autowired
    LikeDao likedao;

    @Autowired
    Viewdao viewdao;

    @Autowired
    VisitorDao visitordao;

    @GetMapping(value = "/visitorall")
    @ApiOperation(value = "방문자 리스트 반환 : ( 날짜 , 방문자수)로 구성")
    public Object visitorall() {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        List<visitor> visitorlist = visitordao.findAll();
        if (visitorlist == null || visitorlist.isEmpty()) {
            result.put("data", "fail");
        } else {
            result.put("data", "success");
            result.put("object", visitorlist);
        }
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    // wordcloud 통계
    @GetMapping(value = "/getwordcloud")
    @ApiOperation(value = "워드클라우드 단어 <-> 빈도수의 json객체를 얻어옴")
    public Object wordCloude() {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        List<typofrequencywrapperclass> wordcloudlist = null;
        List<typo> typoList = typodao.findByOrderByWordcntDesc();
        if (typoList == null || typoList.isEmpty()) {
            result.put("data", "fail");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            wordcloudlist = new ArrayList<>();
            for (typo typo : typoList) {
                // 빈도수를 파싱하여 워드클라우드의 형태로 보내준다.
                wordcloudlist.add(new typofrequencywrapperclass(typo.getWord(), typo.getWordcnt()));
            }
            result.put("data", "success");
            result.put("object", wordcloudlist);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    // 최근 7일의 인기글 통계 (조회순)

    @GetMapping("/getHotpostforWeek")
    @ApiOperation(value = "최근 7일의 조회순의 게시글 5개")
    public Object getHostpostforWeek() {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();

        List<PostidandviewcntProjectionInterface> posttitlelist = viewdao
                .findPostidAndViewCntByTime(LocalDateTime.now(), LocalDateTime.now().minusDays(7));

        System.out.println(posttitlelist.get(0).getViewcnt());
        List<posttitleandviewcntwrapperclass> HotpostList = new ArrayList<>();

        if (posttitlelist == null || posttitlelist.isEmpty()) {

            result.put("data", "fail");
            result.put("message", "최근 7일의 조회수 통계 로딩 실패");

            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            for (int i = 0; i < posttitlelist.size(); i++) {

                PostidandviewcntProjectionInterface now = posttitlelist.get(i);
                posttitleandviewcntwrapperclass wrapper = new posttitleandviewcntwrapperclass(i + 1,
                        postdao.findOne(now.getPostid()).getTitle(), now.getViewcnt());
                HotpostList.add(wrapper);
            }

            statisticviewWrapperClass statisticviewWrapperclass = new statisticviewWrapperClass();
            statisticviewWrapperclass.setLabel("최근 7일의 조회수가 가장많은 포스트");
            statisticviewWrapperclass.setBackgroundColor("rgba(53,175,230,0.4)");
            statisticviewWrapperclass.setHoverBorderColor("rgba(53,175,230,0.4)");
            statisticviewWrapperclass.setHoverBackgroundColor("rgba(53,175,230,0.4)");
            statisticviewWrapperclass.setBorderWidth(1);
            statisticviewWrapperclass.setBorderColor("rgba(53,175,230,0.4)");
            statisticviewWrapperclass.setBorderCapStyle("round");

            List<Integer> data = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            int n = 0;
            for (posttitleandviewcntwrapperclass wrapper : HotpostList) {
                data.add(wrapper.getViewcnt());
                labels.add(wrapper.getPosttitle());
                if (n > 5)
                    break;
                else
                    n++;
            }

            statisticviewWrapperclass.setData(data);
            statisticViewSenderWrapperClass returnval = new statisticViewSenderWrapperClass();
            returnval.setLabels(labels);
            returnval.setStatisticviewWrapperclass(statisticviewWrapperclass);
            result.put("data", "success");
            result.put("message", "최근 7일의 조회수 통계 로딩 성공");
            result.put("object", returnval);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    // 가장 좋아요가 많은 포스트 목록
    @GetMapping("/getMostLikeforWeek")
    @ApiOperation(value = "최근 7일의 좋아요 게시글 5개")
    public Object getMostLikeforWeek() {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();

        List<Integer> postidlist = likedao.findPostidlistOrderLike();
        List<statiscPostLikeWrapperClass> statiscPostLikeWrapperClassList = new ArrayList<>();

        if (postidlist == null || postidlist.isEmpty()) {

            result.put("data", "fail");
            result.put("message", "좋아요 순의 게시글중 오류 발생");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            for (Integer postid : postidlist) {
                statiscPostLikeWrapperClass wrapper = new statiscPostLikeWrapperClass();
                wrapper.setPosttitle(postdao.findOne(postid).getTitle());
                wrapper.setLikecnt(likedao.countByPostid(postid));
                statiscPostLikeWrapperClassList.add(wrapper);
            }
        }

        if (statiscPostLikeWrapperClassList != null && !statiscPostLikeWrapperClassList.isEmpty()) {

            statisticviewWrapperClass statisticviewWrapperclass = new statisticviewWrapperClass();
            statisticviewWrapperclass.setLabel("최근 7일의 좋아요가 가장 많은 포스트");
            statisticviewWrapperclass.setBackgroundColor("rgba(41,230,185,0.4)");
            statisticviewWrapperclass.setHoverBorderColor("rgba(41,230,185,0.4)");
            statisticviewWrapperclass.setHoverBackgroundColor("rgba(41,230,185,0.4)");
            statisticviewWrapperclass.setBorderWidth(1);
            statisticviewWrapperclass.setBorderColor("rgba(41,230,185,0.4)");
            statisticviewWrapperclass.setBorderCapStyle("round");

            List<Integer> data = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            int n = 0;
            for (statiscPostLikeWrapperClass wrapper : statiscPostLikeWrapperClassList) {
                data.add((int) wrapper.getLikecnt());
                labels.add(wrapper.getPosttitle());
                if (n > 5)
                    break;
                else
                    n++;
            }

            statisticviewWrapperclass.setData(data);
            statisticViewSenderWrapperClass returnval = new statisticViewSenderWrapperClass();
            returnval.setLabels(labels);
            returnval.setStatisticviewWrapperclass(statisticviewWrapperclass);
            result.put("data", "success");
            result.put("message", "최근 7일의 좋아요게시글 통계 로딩 성공");
            result.put("object", returnval);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "fail");
            result.put("message", "최근 7일의 좋아요게시글 통계 로딩 실패");

            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }
}