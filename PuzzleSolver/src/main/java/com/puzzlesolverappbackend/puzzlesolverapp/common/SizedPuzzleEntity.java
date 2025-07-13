package com.puzzlesolverappbackend.puzzlesolverapp.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public abstract class SizedPuzzleEntity extends BasePuzzleEntity {

    @Column(name = "height")
    protected Integer height;

    @Column(name = "width")
    protected Integer width;

    protected SizedPuzzleEntity(String filename, String source, Double difficulty,
                             Integer height, Integer width) {
        super(filename, source, difficulty);
        this.height = height;
        this.width = width;
    }
}