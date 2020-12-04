package com.web.blog.dto.curriculum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curriculumtable", schema = "common_pjt")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(curriculumCompositeKey.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class curriculum {

    @Id
    @Column(name = "curriculumlargescale")
    private String curriculumlargescale;

    @Id
    @Column(name = "curriculummediumscale")
    private String curriculummediumscale;

    @Id
    @Column(name = "curriculumsmallscale")
    private String curriculumsmallscale;

    private Integer nodeid;
    private String parentid;
    private String link;
    private String Contents;
}