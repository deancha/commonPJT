package com.web.blog.dto.crawling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class crawling {

    private String author;
    private String URL;
    private String title;

    @Override
    public String toString() {
        return "crawling [URL=" + URL + ", author=" + author + ", title=" + title + "]";
    }

}