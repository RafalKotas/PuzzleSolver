package com.puzzlesolverappbackend.puzzlesolverapp.sudoku;

import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.BasePuzzleEntity;
import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Publication;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class Sudoku extends BasePuzzleEntity {

    @Embedded
    private Publication publication;

    @Column(name = "filled")
    private Integer filled;

    public Sudoku(String filename, String source, String year, String month,
                  Double difficulty, Integer filled) {
        super(filename, source, difficulty);
        this.publication = new Publication(year, month);
        this.filled = filled;
    }
}
