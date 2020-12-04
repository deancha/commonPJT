package com.web.blog.service.ConvertJsonFormat;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class jsonwrapper {

    private ArrayList<node> nodes;
    private ArrayList<link> links;
}