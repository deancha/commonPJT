package com.web.blog.dto.comments;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "commentstable")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class comment {

    @Id
    private int commentsid;

    private String useremail;
    private String username;
    private String contents;
    private int postid;
    private LocalDateTime createdat;
    private LocalDateTime updatedat;

}