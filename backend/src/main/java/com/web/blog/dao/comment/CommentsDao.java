package com.web.blog.dao.comment;

import java.util.List;
import java.util.Optional;

import com.web.blog.dto.comments.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CommentsDao extends JpaRepository<comment, String> {
    // 시간순으로 최신 댓글이 앞에오고 , postid 기준 달린거 가져옴
    @Query(value = "SELECT * FROM commentstable c WHERE c.postid= ?1 ORDER BY createdat", nativeQuery = true)
    List<comment> findCommentByPostid(int postid);

    comment findByCommentsid(int commentsid);

    Optional<comment> findCommentByCommentsid(int commentsid);

    @Modifying
    @Query(value = "DELETE c FROM commentstable c WHERE c.useremail=?1", nativeQuery = true)
    void deleteAllByUseremailQuery(String targetuseremail);

    @Modifying
    @Query(value = "DELETE c FROM commentstable c WHERE c.postid=?1", nativeQuery = true)
    void deleteAllByPostidQuery(int targetpostid);

    @Query(value = "SELECT postid FROM commentstable c WHERE c.useremail=?1", nativeQuery = true)
    List<Integer> findallUserCommentedpostids(String useremail);

    // postid의 전체 댓글 수 리턴
    long countByPostid(int postId);

    List<comment> findCommentByPostidAndUseremail(int postid, String useremail);
}