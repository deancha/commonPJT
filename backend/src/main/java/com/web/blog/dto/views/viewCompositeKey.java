package com.web.blog.dto.views;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class viewCompositeKey implements Serializable {

    private int postid;
    private LocalDateTime viewdate;

}