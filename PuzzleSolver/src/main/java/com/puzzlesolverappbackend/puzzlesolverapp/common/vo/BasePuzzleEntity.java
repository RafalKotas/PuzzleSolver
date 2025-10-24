package com.puzzlesolverappbackend.puzzlesolverapp.common.vo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@ToString
public abstract class BasePuzzleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "filename")
    protected String filename;

    @Column(name = "source")
    protected String source;

    @Column(name = "difficulty")
    protected Double difficulty;

    protected BasePuzzleEntity(String filename, String source, Double difficulty) {
        this.filename = filename;
        this.source = source;
        this.difficulty = difficulty;
    }
}
