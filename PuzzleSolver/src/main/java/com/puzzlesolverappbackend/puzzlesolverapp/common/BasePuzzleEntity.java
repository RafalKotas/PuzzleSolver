package com.puzzlesolverappbackend.puzzlesolverapp.common;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public BasePuzzleEntity(String filename, String source, Double difficulty) {
        this.filename = filename;
        this.source = source;
        this.difficulty = difficulty;
    }
}
