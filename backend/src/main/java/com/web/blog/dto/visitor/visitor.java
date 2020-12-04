package com.web.blog.dto.visitor;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "visitortable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class visitor {

    @Id
    private LocalDate date;
    private int visitors;

    
}