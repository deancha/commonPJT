package com.web.blog.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class postandlikeandcommentWrapperClass {

    private Post post;
    private long likeCount;
    private long commentCount;
    private int viewCount;

}