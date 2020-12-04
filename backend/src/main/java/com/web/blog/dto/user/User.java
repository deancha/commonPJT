// 하단 DB 설정 부분은 Sub PJT II에서 데이터베이스를 구성한 이후에 주석을 해제하여 사용.

package com.web.blog.dto.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.hibernate.annotations.ColumnDefault;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usertable", schema = "common_pjt")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private String username;
    @Id
    private String useremail;
    private String password;
    private String phonenumber;
    private String profileimg;
    private String introduction;
    private int age;
    private String gender;
    private String isadmin;
    private String issocial;

}
