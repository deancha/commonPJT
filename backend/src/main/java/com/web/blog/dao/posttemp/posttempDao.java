package com.web.blog.dao.posttemp;

import java.util.List;
import java.util.Optional;

import com.web.blog.dto.posttemp.posttemp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface posttempDao extends JpaRepository<posttemp, String> {

    // useremail에 해당되는 모든 임시저장 게시글 반환
    @Query(value = "SELECT * FROM posttemptable p WHERE p.useremail=?1", nativeQuery = true)
    List<posttemp> findByUseremail(String Useremail);

    @Modifying
    @Query(value = "DELETE p FROM posttemptable p WHERE p.useremail=?1", nativeQuery = true)
    void deleteAllByUseremailQuery(String targetuseremail);

    void deleteByPostid(int postid);
    Optional<posttemp> findByPostid(int postid);
}