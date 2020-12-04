package com.web.blog.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class posttitleandviewcntwrapperclass {

    private int ordering;
    private String posttitle;
    private int viewcnt;
}