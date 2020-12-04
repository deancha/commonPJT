package com.web.blog.dto.comments;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class commentanduserWrapperClass {

    private comment comment;
    private String profileimg;
}