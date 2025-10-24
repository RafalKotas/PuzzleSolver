package com.puzzlesolverappbackend.puzzlesolverapp.akari;

import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.BasePuzzleEntity;
import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Dimensions;
import jakarta.persistence.Embedded;
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
@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class Akari extends BasePuzzleEntity {

    @Embedded
    private Dimensions size;

    public Akari(String filename, String source, Double difficulty,
                 Integer height, Integer width) {
        super(filename, source, difficulty);
        this.size = new Dimensions(height, width);
    }
}
