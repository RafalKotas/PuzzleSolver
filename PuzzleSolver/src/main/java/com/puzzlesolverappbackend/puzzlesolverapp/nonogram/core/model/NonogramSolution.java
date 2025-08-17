package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class NonogramSolution {
    List<List<String>> nonogramBoard;
}
