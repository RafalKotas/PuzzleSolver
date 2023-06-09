package com.puzzlesolverappbackend.puzzleAppFileManager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.persistence.metamodel.StaticMetamodel;

@Entity
@Table(name = "architectPuzzlesData")
@StaticMetamodel(Architect.class)
@ToString
@NoArgsConstructor
@Setter
@Getter
public class Architect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    public Architect(String architectFileName, String source, String year, String month, Double difficulty, Integer height, Integer width) {
        this.filename = architectFileName;
        this.source = source;
        this.year = year;
        this.month = month;
        this.difficulty = difficulty;
        this.height = height;
        this.width = width;
    }

    public int getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getSource() {
        return source;
    }

    public String getMonth() {
        return month;
    }

    public Double getDifficulty() {
        return difficulty;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }
}
