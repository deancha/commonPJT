package com.web.blog.dto.students;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class studentsheetwrapperClass {

    private String curriculumlargescale;
    private String curriculummdiumscale;

    private List<String> studentName;
    private List<String> curriculumsmallscale;

    private String[][] studentsheet;
}