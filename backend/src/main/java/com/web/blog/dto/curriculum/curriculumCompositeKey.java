package com.web.blog.dto.curriculum;

import java.io.Serializable;

import lombok.Data;

@Data
public class curriculumCompositeKey implements Serializable {

    private String curriculumlargescale;

    private String curriculummediumscale;

    private String curriculumsmallscale;

}