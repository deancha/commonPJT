package com.web.blog.dto.statistic;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class statisticViewSenderWrapperClass {
    private List<String> labels;
    private statisticviewWrapperClass statisticviewWrapperclass;
}