package com.web.blog.dao.view;

import java.util.List;

import javax.transaction.Transactional;

import com.web.blog.dto.views.unloginview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Unloginviewdao extends JpaRepository<unloginview, String> {

    unloginview findUnloginviewByPostid(int postid);

    // post id 의 조회수 반환
    @Query(value = "SELECT COUNT(*) FROM unloginviewposttable WHERE postid=?1", nativeQuery = true)
    int CountBypostid(int postid);

    @Transactional
    @Modifying
    void deleteAllBypostid(int postid);

    @Transactional
    @Modifying
    @Query(value = "DELETE u FROM unloginviewposttable u WHERE u.postid IN :postids", nativeQuery = true)
    void deleteAllByPostidInQuery(@Param("postids") List<Integer> postids);
}