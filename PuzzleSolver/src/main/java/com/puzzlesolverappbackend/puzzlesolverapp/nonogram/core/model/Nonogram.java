package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import com.puzzlesolverappbackend.puzzlesolverapp.common.TimedPuzzleEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
public class Nonogram extends TimedPuzzleEntity {

    public Nonogram(String filename, String source, String year, String month,
                    Double difficulty, Integer height, Integer width) {
        super(filename, source, year, month, difficulty, height, width);
    }
}
