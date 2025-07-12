package com.puzzlesolverappbackend.puzzlesolverapp.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class SizedPuzzleEntity extends BasePuzzleEntity {

    @Column(name = "height")
    protected Integer height;

    @Column(name = "width")
    protected Integer width;

    public SizedPuzzleEntity(String filename, String source, Double difficulty,
                             Integer height, Integer width) {
        super(filename, source, difficulty);
        this.height = height;
        this.width = width;
    }
}