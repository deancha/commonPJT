package com.web.blog.dto.post;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class postandpopularWrapperClass {

    private String hashtagname;
    private List<Post> posts;

}