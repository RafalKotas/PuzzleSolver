package com.puzzlesolverappbackend.puzzlesolverapp.common.vo;

import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class SizedPublishedEntity extends BasePuzzleEntity {

    @Embedded
    protected Dimensions dimensions;

    @Embedded
    protected Publication publication;

    protected SizedPublishedEntity(String filename, String source, Double difficulty,
                                   Integer height, Integer width,
                                   String year, String month) {
        super(filename, source, difficulty);
        this.dimensions = new Dimensions(height, width);
        this.publication = new Publication(year, month);
    }
}
