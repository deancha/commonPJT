package com.web.blog.dto.hashtag;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hashtagtable", schema = "common_pjt")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(hashtagCompositeKey.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class hashtag {

    @Id
    @Column(name = "postid")
    private int postid;

    @Id
    @Column(name = "word")
    private String word;

}
