package com.web.blog.controller.post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.web.blog.dao.comment.CommentsDao;
import com.web.blog.dao.hashtag.HashTagDao;
import com.web.blog.dao.like.LikeDao;
import com.web.blog.dao.post.PostDao;
import com.web.blog.dao.typo.typoDao;
import com.web.blog.dao.view.Unloginviewdao;
import com.web.blog.dao.view.Viewdao;
import com.web.blog.dto.comments.comment;
import com.web.blog.dto.hashtag.hashtag;
import com.web.blog.dto.post.Post;
import com.web.blog.dto.post.postAndCommentWrapperClass;
import com.web.blog.dto.post.postAndhashtagWrapperClass;
import com.web.blog.dto.post.postAnduserWrapperClass;
import com.web.blog.dto.post.postandlikeandcommentWrapperClass;
import com.web.blog.dto.post.postandpopularWrapperClass;
import com.web.blog.dto.typo.typo;
import com.web.blog.dto.user.User;
import com.web.blog.dto.views.unloginview;
import com.web.blog.dto.views.view;
import com.web.blog.service.MEDCompute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
public class PostController {

    @Autowired
    PostDao postdao;

    // delete 를 위한 dao
    @Autowired
    LikeDao likedao;

    @Autowired
    CommentsDao commentsdao;

    @Autowired
    HashTagDao hashtagdao;

    @Autowired
    typoDao typodao;

    @Autowired
    Viewdao viewdao;

    @Autowired
    Unloginviewdao unloginviewdao;

    @GetMapping("/findbyhashtag")
    @ApiOperation("hashtag를 달고 있는 post를 모두 찾음")
    public Object selectPostByHashtags(@RequestParam String word){
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        
        List<Integer> postids = hashtagdao.findHashtagByWord(word);
        
        List<Post> postlist = new LinkedList<>();
        for (Integer postid : postids) {
            Post  post = postdao.findOne(postid);
            postlist.add(post);
        }

        for (Post post : postlist) {
            System.out.println(post.getTitle());
        }

        ArrayList<postandlikeandcommentWrapperClass> warpperlist = new ArrayList();
        for (Post post  : postlist) {
            long likecnt = likedao.countByPostid(post.getPostid());
            long commentcnt = commentsdao.countByPostid(post.getPostid());
            int viewcnt = viewdao.CountBypostid(post.getPostid());
            int unloginviewcnt = unloginviewdao.CountBypostid(post.getPostid());

            postandlikeandcommentWrapperClass wrapper = new postandlikeandcommentWrapperClass();
               

            wrapper.setPost(post);
            wrapper.setCommentCount(commentcnt);
            wrapper.setLikeCount(likecnt);
            wrapper.setViewCount(viewcnt + unloginviewcnt);
            warpperlist.add(wrapper);

            }
        result.put("message", "조회 완료");
        result.put("data", "success");
        result.put("object", warpperlist);
        
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    // 목록 조회
    @GetMapping("/post")
    @ApiOperation(value = "전체조회 기능 및 작성자, 제목으로 검색 ")
    public Object selectAllofPost(@RequestParam(required = false) String key,
            @RequestParam(required = false) String word) {
        System.out.println("===================================================");
        System.out.println("key : " + key);
        System.out.println("word : " + word);

        System.out.println("===================================================");
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        List<Post> list = null;
        // 전체조회

        if (word == null) {
            list = postdao.findAllByOrderByCreatedatDesc();
        } else if (key.equals("useremail")) {
            list = postdao.findByUseremailContainingOrderByCreatedatDesc(word);
        } else if (key.equals("title")) {
            list = postdao.findByTitleContainingOrderByCreatedatDesc(word);
        } else if (key.equals("username")) {
            list = postdao.findByUsernameContainingOrderByCreatedatDesc(word);
        }
        if (list.size() > 0) {

            if (key != null && key.equals("title")) {
                if (!typodao.findById(word).isPresent()) {
                    // 검색 단어가 db에 없으면?
                    typo saveObject = new typo();
                    saveObject.setWord(word);
                    saveObject.setSearchingdate(LocalDateTime.now());
                    saveObject.setWordcnt(0);
                    typodao.save(saveObject);
                } else {
                    // 있으면
                    typo now = typodao.findByWord(word);
                    now.setWordcnt(now.getWordcnt() + 1);
                    typodao.save(now);
                }
            }
            // wrapperclass의 list로 재 정의해야한다.
            // 현재의 postid로 count한 wrapper의 list를 반환
            ArrayList<postandlikeandcommentWrapperClass> warpperlist = new ArrayList();
            for (Post post : list) {
                long likecnt = likedao.countByPostid(post.getPostid());
                long commentcnt = commentsdao.countByPostid(post.getPostid());

                int viewcnt = viewdao.CountBypostid(post.getPostid());
                int unloginviewcnt = unloginviewdao.CountBypostid(post.getPostid());

                postandlikeandcommentWrapperClass wrapper = new postandlikeandcommentWrapperClass();
                System.out.println("likecnt : " + likecnt);
                System.out.println("commentcnt : " + commentcnt);

                wrapper.setPost(post);
                wrapper.setCommentCount(commentcnt);
                wrapper.setLikeCount(likecnt);
                wrapper.setViewCount(viewcnt + unloginviewcnt);
                warpperlist.add(wrapper);
            }
            result.put("message", "조회 완료");
            result.put("data", "success");
            result.put("object", warpperlist);
        } else {
            int res = -1;
            List<typo> Wordlist = null;
            Wordlist = typodao.findAll();
            // 성능향상을 위해 db에서 unique한 값만 가져오는 쿼리를 정의해야한다 08-12
            // 오탈자 검색이 들어가야하는 부분 07.27 서상원
            // 최대 변경 횟수는 자모수의 글자수/2 만큼
            // Ex) 사 과 -> 2번 바꿔서 사과가 나오면 오타라고 인식
            int minDistance = 1234567891;
            int listsize = Wordlist.size();
            MEDCompute computer = new MEDCompute();
            for (int i = 0; i < listsize; i++) {
                int size = (Wordlist.get(i).getWord().length());
                int Distance = computer.Compute_MED(word, Wordlist.get(i).getWord());
                System.out.println("보정한 거리 Distance :" + Distance);
                System.out.println("단어 size :" + size);
                if (size > Distance && minDistance > Distance) {
                    minDistance = Distance;
                    res = i;
                }
            }

            if (res == -1) {
                result.put("message", "조회 실패");
                result.put("data", "fail");
                result.put("object", null);
            } else {
                result.put("message", "수정 성공 제안해야합니다..");
                result.put("object", Wordlist.get(res));
                result.put("data", "modify");
            }
        }

        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;

    }

    // 상세조회
    @PostMapping("/selectpost")
    @ApiOperation(value = "postid로 포스트 상세조회")
    public Object selectOnePost(@RequestBody postAnduserWrapperClass postAnduserWrapperclass) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        unloginview resunloginview = null;
        view ressaveview = null;
        Post respost = null;
        if (postAnduserWrapperclass.getUseremail() != null) {
            // 로그인 상황
            view saveview = new view();
            saveview.setPostid(postAnduserWrapperclass.getPostid());
            saveview.setUseremail(postAnduserWrapperclass.getUseremail());
            saveview.setViewdate(LocalDateTime.now().plusHours(9));
            saveview.setUsername(postAnduserWrapperclass.getUsername());
            ressaveview = viewdao.save(saveview);
        } else {
            // 비로그인 상황
            unloginview unloginview = unloginviewdao.findUnloginviewByPostid(postAnduserWrapperclass.getPostid());
            unloginview.setViews(unloginview.getViews() + 1);
            resunloginview = unloginviewdao.save(unloginview);
        }
        respost = postdao.findOne(postAnduserWrapperclass.getPostid());
        if (respost == null) {
            result.put("data", "fail");
            result.put("message", "포스트 정보 조회  실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else if (resunloginview == null && ressaveview == null) {
            // fail
            result.put("data", "fail");
            result.put("message", "조회수 증가 실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "success");
            String resultStr = "";
            if (resunloginview == null)
                resultStr = "로그인 상태";
            else
                resultStr = "비로그인 상태";
            result.put("message", "포스트 정보 조회 성공 / +" + resultStr + "+ 조회수 1 증가");
            result.put("object", respost);
            response = new ResponseEntity<>(result, HttpStatus.OK);

        }

        return response;

    }

    // 포스트 insert
    @PostMapping("/post")
    @ApiOperation(value = "포스트 등록")
    public Object insertPost(@RequestBody postAndhashtagWrapperClass wrapper) {

        Post newpost = wrapper.getPost();
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        newpost.setCreatedat(LocalDateTime.now().plusHours(9));

        Post postresult = postdao.save(newpost);

        if (postresult == null) {
            result.put("status", false);
            result.put("data", "fail");
            result.put("message", "포스트 등록 실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            int postid = postresult.getPostid();
            unloginviewdao.save(new unloginview(postresult.getPostid(), 0));
            if (wrapper.getHashtags() != null) {
                for (String hash : wrapper.getHashtags()) { // 그 포스트 아이디로 hashtag저장
                    hashtagdao.save(new hashtag(postid, hash));
                }
            }
            result.put("status", true);
            result.put("data", "success");
            result.put("message", "포스트 등록성공");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    // 포스트 update - postId로 존재하는지 확인 후 수정
    @PutMapping("/post")
    @ApiOperation(value = "postId로 포스트 수정")
    public Object updatePost(@RequestBody Post post) {

        ResponseEntity res = null;
        HashMap<String, Object> result = new HashMap<>();
        Optional<Post> prepost = postdao.findByPostid(post.getPostid());

        if (prepost.isPresent()) {
            post.setUpdatedat(LocalDateTime.now().plusHours(9));
            postdao.save(post);
            result.put("data", "success");
            result.put("message", "포스트 수정 성공");
        } else {
            result.put("data", "fail");
            result.put("message", "포스트 수정실패");
        }

        res = new ResponseEntity<>(result, HttpStatus.OK);
        return res;

    }

    // 포스트 delete - postId로 삭제
    @DeleteMapping("/post")
    @Transactional
    @ApiOperation(value = "postId로 포스트 삭제")
    public Object deletePost(@RequestParam(required = true) int postid) {

        ResponseEntity response = null;
        final HashMap<String, Object> result = new HashMap<>();

        Optional<Post> check = postdao.findByPostid(postid);
        if (check.isPresent()) {
            viewdao.deleteAllBypostid(postid);
            unloginviewdao.deleteAllBypostid(postid);
            likedao.deleteAllByPostidQuery(postid);
            hashtagdao.deleteAllByPostidQuery(postid);
            commentsdao.deleteAllByPostidQuery(postid);
            postdao.deleteByPostid(postid);
            result.put("data", "success");
            result.put("message", "포스트 삭제 성공");

            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            // 삭제 실패
            result.put("data", "fail");
            result.put("message", "포스트 삭제 실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @PostMapping("/getUserLikedPosts")
    @ApiOperation(value = "사용자가 좋아요 눌렀던 post목록 반환")
    public Object getUesrLikedPosts(@RequestBody User user) {
        List<postandlikeandcommentWrapperClass> likeandCommentandViewList = null;
        List<Post> posts = null;
        ResponseEntity response = null;
        final HashMap<String, Object> result = new HashMap<>();
        List<Integer> postids = likedao.findAllpostidListByUseremail(user.getUseremail());
        if (postids != null) {
            // System.out.println(postids.get(postids.size() - 1));
            posts = postdao.findAllUserLikeClickedPosts(postids);

        }
        if (postids == null || posts == null) {

            result.put("data", "fail");
            result.put("message", "user가 좋아요를 누른 게시글이 없음");

            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            likeandCommentandViewList = new ArrayList<>();
            for (Post post : posts) {
                postandlikeandcommentWrapperClass postclass = new postandlikeandcommentWrapperClass();
                postclass.setPost(post);
                long likecnt = likedao.countByPostid(post.getPostid());
                long commentcnt = commentsdao.countByPostid(post.getPostid());
                postclass.setLikeCount(likecnt);
                postclass.setCommentCount(commentcnt);
                postclass.setViewCount(
                        viewdao.CountBypostid(post.getPostid()) + unloginviewdao.CountBypostid(post.getPostid()));
                likeandCommentandViewList.add(postclass);
            }
            result.put("object", likeandCommentandViewList);
            result.put("data", "success");
            result.put("message", "user가 좋아요 누른 게시글 리스트 반환 성공");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @PostMapping("/getUserCommentedPosts")
    @ApiOperation(value = "사용자가 댓글을 남겼던 post목록과 댓글 내용 반환")
    public Object getUesrCommentedPosts(@RequestBody User user) {
        List<Post> posts = null;
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        List<Integer> postids = commentsdao.findallUserCommentedpostids(user.getUseremail());
        if (postids != null && !postids.isEmpty()) {
            // System.out.println(postids.get(postids.size() - 1));
            posts = postdao.findAllUserLikeClickedPosts(postids);

        }
        if (postids == null || posts == null || postids.isEmpty() || posts.isEmpty()) {

            result.put("data", "fail");
            result.put("message", "user가 댓글을 단 게시글이 없음");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            System.out.println(posts.toString());
            List<postAndCommentWrapperClass> postAndCommentWrapperList = new ArrayList<>();

            for (Post post : posts) {
                postandlikeandcommentWrapperClass postclass = new postandlikeandcommentWrapperClass();
                postclass.setPost(post);
                long likecnt = likedao.countByPostid(post.getPostid());
                long commentcnt = commentsdao.countByPostid(post.getPostid());

                postclass.setLikeCount(likecnt);
                postclass.setCommentCount(commentcnt);
                postclass.setViewCount(
                        viewdao.CountBypostid(post.getPostid()) + unloginviewdao.CountBypostid(post.getPostid()));

                List<comment> commentList = commentsdao.findCommentByPostidAndUseremail(post.getPostid(),
                        user.getUseremail());
                for (comment comment : commentList) {
                    postAndCommentWrapperClass postAndCommentWrapperclass = new postAndCommentWrapperClass();
                    postAndCommentWrapperclass.setComment(comment);
                    postAndCommentWrapperclass.setPostandlikeandcommentWrapperclass(postclass);
                    postAndCommentWrapperList.add(postAndCommentWrapperclass);
                }
            }
            result.put("object", postAndCommentWrapperList);
            result.put("data", "success");
            result.put("message", "user가 댓글을 단 게시글 목록 반환 성공");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @PostMapping("/getPopluarpostrelated")
    @ApiOperation(value = "상세 페이지로 접근했을 때, 그 페이지에 있는 해쉬테그와 관련있으면서 좋아요가 가장높은 게시글 리스트 가져옴")
    public Object getPopluarpostrelated(@RequestBody Post post) {
        List<Post> posts = null;
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        List<hashtag> hashtaglist = hashtagdao.findHashtagByPostid(post.getPostid());
        ArrayList<postandpopularWrapperClass> popluarPost = new ArrayList<>();
        for (hashtag hashtag : hashtaglist) {
            List<Integer> postidlist = hashtagdao.findHashtagByWord(hashtag.getWord());
            List<Integer> popularpostidlist = likedao.findByPostidList(postidlist);
            List<Post> popularpostlist = postdao.findAllUserLikeClickedPosts(popularpostidlist);
            postandpopularWrapperClass postpopular = new postandpopularWrapperClass();
            postpopular.setHashtagname(hashtag.getWord());
            postpopular.setPosts(popularpostlist);
            if (popluarPost.size() < 3) {
                popluarPost.add(postpopular);
            } else {
                break;
            }
        }
        if (popluarPost == null || popluarPost.size() == 0) {

            result.put("data", "fail");
            result.put("message", "관련있는 게시글 없음");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {

            result.put("data", "success");
            result.put("message", "관련있는 게시글 리스트 로딩");
            result.put("object", popluarPost);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @GetMapping("/getLikeOrder")
    @ApiOperation(value = "좋아요순의 게시글 출력")
    public Object getLikeOrder() {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        ArrayList<postandlikeandcommentWrapperClass> warpperlist = new ArrayList();
        List<Integer> postidlist = likedao.findPostidlistOrderLike();
        // 자를거면 여기서 몇개만 잘라서 쓰면된다.
        if (postidlist == null || postidlist.isEmpty()) {

            result.put("data", "fail");
            result.put("message", "좋아요 순의 게시글중 오류 발생");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            for (Integer postid : postidlist) {
                long likecnt = likedao.countByPostid(postid);
                long commentcnt = commentsdao.countByPostid(postid);
                postandlikeandcommentWrapperClass wrapper = new postandlikeandcommentWrapperClass();
                wrapper.setPost(postdao.findOne(postid));
                wrapper.setCommentCount(commentcnt);
                wrapper.setLikeCount(likecnt);

                warpperlist.add(wrapper);
            }

            result.put("data", "success");
            result.put("message", "좋아요 순의 게시글 리스트 완료");
            result.put("object", warpperlist);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @GetMapping("/getHotpost")
    @ApiOperation(value = "조회순의 게시글 출력3개")
    public Object getHostpost() {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        // 자를거면 여기서 몇개만 잘라서 쓰면된다.

        List<Post> postlist = postdao.findAllByOrderByCreatedatDesc();
        ArrayList<postandlikeandcommentWrapperClass> ViewOrderedList = new ArrayList<>();
        for (Post post : postlist) {
            postandlikeandcommentWrapperClass postandlikeandcommentWrapperclass = new postandlikeandcommentWrapperClass();
            int viewcnt = viewdao.CountBypostid(post.getPostid());
            int unloginviewcnt = unloginviewdao.CountBypostid(post.getPostid());
            postandlikeandcommentWrapperclass.setViewCount(viewcnt + unloginviewcnt);
            postandlikeandcommentWrapperclass.setPost(post);
            ViewOrderedList.add(postandlikeandcommentWrapperclass);

        }
        if (ViewOrderedList == null || ViewOrderedList.isEmpty()) {

            result.put("data", "fail");
            result.put("message", "조회순 기준 정렬 실패");

            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            Collections.sort(ViewOrderedList, new Comparator<postandlikeandcommentWrapperClass>() {
                @Override
                public int compare(postandlikeandcommentWrapperClass o1, postandlikeandcommentWrapperClass o2) {
                    return o2.getViewCount() - o1.getViewCount();
                }
            });
            result.put("data", "success");
            result.put("message", "조회 순의 게시글 리스트 완료");
            result.put("object", ViewOrderedList);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

}
