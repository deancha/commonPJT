package com.web.blog.dao.view;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import com.web.blog.dto.post.PostidandviewcntProjectionInterface;
import com.web.blog.dto.post.posttitleandviewcntwrapperclass;
import com.web.blog.dto.views.view;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface Viewdao extends JpaRepository<view, String> {

    // 한 사람이 조회한 게시글 최신순으로 목록반환
    @Query(value = "SELECT postid FROM viewposttable WHERE useremail =?1 GROUP BY postid ORDER BY MAX(viewdate) DESC", nativeQuery = true)
    List<Integer> findPostidByUseremailGroupByPostidOrderByViewdateDesc(String useremail);

    // post id 의 조회수 반환
    @Query(value = "SELECT COUNT(*) FROM viewposttable WHERE postid=?1", nativeQuery = true)
    int CountBypostid(int postid);

    // user가 본 게시글 전부 삭제
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM viewposttable WHERE useremail=?1", nativeQuery = true)
    void DeleteByuseremail(String useremail);

    @Transactional
    @Modifying
    void deleteAllBypostid(int postid);

    @Query(value = "SELECT v.postid AS postid,COUNT(v.postid) AS viewcnt FROM viewposttable AS v WHERE v.viewdate BETWEEN ?2 AND ?1 GROUP BY v.postid ORDER BY COUNT(v.postid) DESC ", nativeQuery = true)
    List<PostidandviewcntProjectionInterface> findPostidAndViewCntByTime(LocalDateTime now, LocalDateTime lastweek);

}