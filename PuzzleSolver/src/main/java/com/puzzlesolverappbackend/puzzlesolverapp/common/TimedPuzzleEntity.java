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
public abstract class TimedPuzzleEntity extends SizedPuzzleEntity {

    @Column(name = "year")
    protected String year;

    @Column(name = "month")
    protected String month;

    protected TimedPuzzleEntity(String filename, String source, String year, String month,
                             Double difficulty, Integer height, Integer width) {
        super(filename, source, difficulty, height, width);
        this.year = year;
        this.month = month;
    }
}