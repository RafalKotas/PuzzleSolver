package com.puzzlesolverappbackend.puzzlesolverapp.akari;

import com.puzzlesolverappbackend.puzzlesolverapp.common.SizedPuzzleEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "akari")
@StaticMetamodel(Akari.class)
@ToString
@NoArgsConstructor
@Setter
@Getter
public class Akari extends SizedPuzzleEntity {

    public Akari(String filename, String source, Double difficulty,
                 Integer height, Integer width) {
        super(filename, source, difficulty, height, width);
    }
}
