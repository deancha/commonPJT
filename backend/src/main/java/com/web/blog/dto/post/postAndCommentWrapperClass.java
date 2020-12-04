package com.web.blog.dto.post;

import com.web.blog.dto.comments.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class postAndCommentWrapperClass {

    private postandlikeandcommentWrapperClass postandlikeandcommentWrapperclass;
    private comment comment;
}