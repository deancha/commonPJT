package com.web.blog.dto.like;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "liketable")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(likeCompositeKey.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class like {
    @Id
    private int postid;
    private String useremail;
    private String username;

}