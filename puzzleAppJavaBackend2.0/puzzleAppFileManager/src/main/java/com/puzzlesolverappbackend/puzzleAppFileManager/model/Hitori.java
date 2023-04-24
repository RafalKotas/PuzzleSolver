package com.puzzlesolverappbackend.puzzleAppFileManager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.persistence.metamodel.StaticMetamodel;

@Entity
@Table(name = "hitoriPuzzlesData")
@StaticMetamodel(Hitori.class)
@ToString
@NoArgsConstructor
@Setter
@Getter
public class Hitori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "source")
    private String source;

    @Column(name = "difficulty")
    private Double difficulty;

    @Column(name = "height")
    private Integer height;

    @Column(name = "width")
    private Integer width;

    public Hitori(String hitoriFileName, String source, Double difficulty, Integer height, Integer width) {
        this.filename = hitoriFileName;
        this.source = source;
        this.difficulty = difficulty;
        this.height = height;
        this.width = width;
    }
}
