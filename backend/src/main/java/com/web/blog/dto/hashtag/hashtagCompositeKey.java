package com.web.blog.dto.hashtag;

import java.io.Serializable;

import lombok.Data;

@Data
public class hashtagCompositeKey implements Serializable {

    public int postid;
    public String word;

}