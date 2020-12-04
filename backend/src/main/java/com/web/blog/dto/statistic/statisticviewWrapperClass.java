package com.web.blog.dto.statistic;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class statisticviewWrapperClass {

    private String label;
    private String backgroundColor;
    private String borderColor;
    private int borderWidth;
    private String hoverBackgroundColor;
    private String hoverBorderColor;
    private String borderCapStyle;
    private List<Integer> data;

}