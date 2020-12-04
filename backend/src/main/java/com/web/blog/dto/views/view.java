package com.web.blog.dto.views;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "viewposttable")
@Data
@IdClass(viewCompositeKey.class)
@NoArgsConstructor
@AllArgsConstructor
public class view {

    @Id
    private int postid;

    @Id
    private LocalDateTime viewdate;

    private String useremail;
    private String username;
}