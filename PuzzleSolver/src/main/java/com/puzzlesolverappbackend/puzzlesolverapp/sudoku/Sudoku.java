package com.puzzlesolverappbackend.puzzlesolverapp.sudoku;

import com.puzzlesolverappbackend.puzzlesolverapp.common.BasePuzzleEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "sudoku")
@StaticMetamodel(Sudoku.class)
@ToString
@NoArgsConstructor
@Setter
@Getter
public class Sudoku extends BasePuzzleEntity {

    @Column(name = "year")
    private String year;

    @Column(name = "month")
    private String month;

    @Column(name = "filled")
    private Integer filled;

    public Sudoku(String filename, String source, String year, String month,
                  Double difficulty, Integer filled) {
        super(filename, source, difficulty);
        this.year = year;
        this.month = month;
        this.filled = filled;
    }
}
