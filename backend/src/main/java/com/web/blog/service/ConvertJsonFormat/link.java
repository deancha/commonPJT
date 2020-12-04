package com.web.blog.service.ConvertJsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class link {
    private String type;
    private int value;
    private int target;
    private int source;
}