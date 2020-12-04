
package com.web.blog.dao.post;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.web.blog.dto.post.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostDao extends JpaRepository<Post, String> {

    List<Post> findByTitleContainingOrderByCreatedatDesc(String title);

    List<Post> findByUseremailContainingOrderByCreatedatDesc(String useremail);

    List<Post> findByUsernameContainingOrderByCreatedatDesc(String username);

    List<Post> findAllByOrderByCreatedatDesc();

    Optional<Post> findByPostid(int postid);

    List<Post> deleteByUseremail(String usermail);

    void deleteByPostid(int postid);

    @Query(value = "SELECT * FROM posttable p WHERE p.postid=?1", nativeQuery = true)
    Post findOne(int postid);

    @Modifying
    @Query(value = "DELETE p FROM posttable p WHERE p.useremail=?1", nativeQuery = true)
    void deleteAllByUseremailQuery(String targetuseremail);

    // user 삭제를 위해서 useremail이 같은 postid 리스트를 반환
    @Query(value = "SELECT p.postid FROM posttable p WHERE p.useremail=?1", nativeQuery = true)
    List<Integer> findPostidlistByUseremail(String Useremail);

    // postidlist에 해당하는 postlist 반환
    @Query(value = "SELECT * FROM posttable WHERE postid IN :postids", nativeQuery = true)
    List<Post> findAllUserLikeClickedPosts(@Param("postids") List<Integer> postids);

}
