package com.web.blog.dto.students;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "studentstable", schema = "common_pjt")
@Data
@NoArgsConstructor
@IdClass(studentCompositeKey.class)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class student {

    @Id
    private String useremail;
    private String username;

    @Id
    private String curriculumlargescale;
    @Id
    private String curriculummediumscale;
    @Id
    private String curriculumsmallscale;
    private String isfinish;

}