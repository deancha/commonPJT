package com.web.blog.dao.hashtag;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.sym.Name;
import com.web.blog.dto.hashtag.hashtag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface HashTagDao extends JpaRepository<hashtag, String> {

    Optional<hashtag> findHashtagByPostidAndWord(int postid, String word);

    List<hashtag> findHashtagByPostid(int postid);

    // 해쉬태그가 포함된 postid list를 가져온다
    @Query(value = "SELECT postid FROM hashtagtable h WHERE h.word = ?1 ", nativeQuery = true)
    List<Integer> findHashtagByWord(String word);

    @Transactional
    @Modifying
    void deleteByPostidAndWord(int postid, String word);

    // user 삭제를 위해서 해당 user가 작성한 postid의 list를 주었을 때, 삭제한다
    @Transactional
    @Modifying
    @Query(value = "DELETE h FROM hashtagtable h WHERE h.postid IN :postids", nativeQuery = true)
    void deleteAllByPostidInQuery(@Param("postids") List<Integer> postids);

    @Transactional
    @Modifying
    @Query(value = "DELETE h FROM hashtagtable h WHERE h.postid=?1", nativeQuery = true)
    void deleteAllByPostidQuery(int postid);

}