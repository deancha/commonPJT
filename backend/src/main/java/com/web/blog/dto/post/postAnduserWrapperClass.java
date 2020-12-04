package com.web.blog.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class postAnduserWrapperClass {

    private int postid;
    private String username;
    private String useremail;
}