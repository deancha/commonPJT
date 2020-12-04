package com.web.blog.dao.like;

import java.util.List;
import java.util.Optional;

import com.web.blog.dto.like.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface LikeDao extends JpaRepository<like, String> {
    // 현재 이메일, postid 로 객체가 있는지?
    Optional<like> findLikeByUseremailAndPostid(String userEmail, int postId);

    // post_id의 전체 좋아요수 반환
    long countByPostid(int postId);

    // 해쉬태그가 포함된 리스트에서 좋아요수가 가장많은 상위 3항목의 post_id를 반환->post_id title을 얻어오면된다.
    @Query(value = "select postid from liketable L where postid IN (:postidList)  group by postid  order by count(useremail) desc", nativeQuery = true)
    List<Integer> findByPostidList(@Param("postidList") List<Integer> postidList);

    @Modifying
    @Query(value = "DELETE L FROM liketable L WHERE L.useremail=?1", nativeQuery = true)
    void deleteAllByUseremailQuery(String targetuseremail);

    @Modifying
    void deleteByUseremailAndPostid(String useremail, int postid);

    @Modifying
    @Query(value = "DELETE L FROM liketable L WHERE L.postid=?1", nativeQuery = true)
    void deleteAllByPostidQuery(int postid);

    // 한명의 user가 누른 좋아요의 게시글 목록 반환
    @Query(value = "SELECT L.postid FROM liketable L WHERE L.useremail=?1", nativeQuery = true)
    List<Integer> findAllpostidListByUseremail(String targetuseremail);

    // 좋아요 순의 postidlist를 반환
    @Query(value = "SELECT postid FROM liketable GROUP BY postid ORDER BY COUNT(postid) DESC", nativeQuery = true)
    List<Integer> findPostidlistOrderLike();
}