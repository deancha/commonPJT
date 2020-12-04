package com.web.blog.dao.typo;

import java.util.List;

import com.web.blog.dto.typo.typo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface typoDao extends JpaRepository<typo, String> {

    @Query(value = "SELECT word FROM typotable t ", nativeQuery = true)
    List<String> findtypoBy();

    List<typo> findByOrderByWordcntDesc();

    typo findByWord(String word);
}