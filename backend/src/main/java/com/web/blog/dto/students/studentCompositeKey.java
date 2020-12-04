package com.web.blog.dto.students;

import java.io.Serializable;

import lombok.Data;

@Data
public class studentCompositeKey implements Serializable {

    private String useremail;
    private String curriculumlargescale;
    private String curriculummediumscale;
    private String curriculumsmallscale;
}