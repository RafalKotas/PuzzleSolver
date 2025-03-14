package com.puzzlesolverappbackend.puzzleAppFileManager.model;

import jakarta.persistence.*;
import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "slitherlink")
@StaticMetamodel(Slitherlink.class)
@ToString
@NoArgsConstructor
@Setter
public class Slitherlink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Slitherlink(String slitherlinkFileNameWithoutExtension, String source, String year, String month, Double difficulty, Integer height, Integer width) {
        this.filename = slitherlinkFileNameWithoutExtension;
        this.source = source;
        this.year = year;
        this.month = month;
        this.difficulty = difficulty;
        this.height = height;
        this.width = width;
    }
}
