package com.web.blog.dto.posttemp;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posttemptable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class posttemp {
    @Id
    private int postid;
    private String title;
    private String useremail;
    private LocalDateTime createdat;
    private LocalDateTime updatedat;
    private String contents;
    private String username;

}