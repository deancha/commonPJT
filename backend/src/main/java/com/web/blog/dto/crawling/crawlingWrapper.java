package com.web.blog.dto.crawling;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class crawlingWrapper {

    private String header;
    private List<crawling> crawlingList;
}