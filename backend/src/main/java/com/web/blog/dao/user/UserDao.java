
package com.web.blog.dao.user;

import java.util.Optional;

import com.web.blog.dto.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User, Object> {
    User getUserByUseremail(String userEmail);
    
    User findUserByUseremailAndPassword(String userEmail, String password);

    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByUseremail(String userEmail);

    @Query(value = "SELECT isadmin FROM usertable u WHERE u.useremail=?1", nativeQuery = true)
    String findByUsereamil(String targetuseremail);

}
