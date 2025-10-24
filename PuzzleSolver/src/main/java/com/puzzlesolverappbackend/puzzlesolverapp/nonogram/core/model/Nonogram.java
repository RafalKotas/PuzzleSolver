package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.SizedPublishedEntity;
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
@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class Nonogram extends SizedPublishedEntity {

    public Nonogram(String filename, String source, String year, String month,
                    Double difficulty, Integer height, Integer width) {
        super(filename, source, difficulty, height, width, year, month);
    }
}
