package com.web.blog.dto.views;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "unloginviewposttable")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class unloginview {

    @Id
    private int postid;
    private int views;
}