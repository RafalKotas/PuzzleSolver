package com.puzzlesolverappbackend.puzzleAppFileManager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.persistence.metamodel.StaticMetamodel;

@Entity
@Table(name = "sudokuPuzzlesData")
@StaticMetamodel(Architect.class)
@ToString
@NoArgsConstructor
@Setter
@Getter
public class Sudoku {

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

    @Column(name = "filled")
    private Integer filled;

    public Sudoku(String architectFileName, String source, String year, String month, Double difficulty, Integer filled) {
        this.filename = architectFileName;
        this.source = source;
        this.year = year;
        this.month = month;
        this.difficulty = difficulty;
        this.filled = filled;
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

    public Integer getFilled() { return filled; }
}
