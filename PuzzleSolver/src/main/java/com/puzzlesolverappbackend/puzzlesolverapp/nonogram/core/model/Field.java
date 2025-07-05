package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Field {

    private int rowIdx;
    private int columnIdx;
}
