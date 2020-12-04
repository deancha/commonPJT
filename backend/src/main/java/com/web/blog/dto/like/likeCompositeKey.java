package com.web.blog.dto.like;

import java.io.Serializable;

import lombok.Data;

@Data
public class likeCompositeKey implements Serializable {

    public int postid;
    public String useremail;

}