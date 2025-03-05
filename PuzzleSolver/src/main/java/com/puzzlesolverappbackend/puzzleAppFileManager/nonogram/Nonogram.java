package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram;

import jakarta.persistence.*;
import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "nonogram")
@StaticMetamodel(Nonogram.class)
@ToString
@NoArgsConstructor
@Setter
@Getter
public class Nonogram {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "source")
    private String source;

    @Column(name = "year")
    private String year;

    @Column(name = "month")
    private String month;

    @Column(name = "difficulty")
    private Double difficulty;

    @Column(name = "height")
    private Integer height;

    @Column(name = "width")
    private Integer width;

    public Nonogram(String filename, String source, String year, String month, Double difficulty, Integer height, Integer width) {
        this.filename = filename;
        this.source = source;
        this.year = year;
        this.month = month;
        this.difficulty = difficulty;
        this.height = height;
        this.width = width;
    }
}
