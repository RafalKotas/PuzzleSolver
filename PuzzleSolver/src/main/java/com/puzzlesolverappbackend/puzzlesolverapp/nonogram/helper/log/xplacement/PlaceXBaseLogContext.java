package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PlaceXBaseLogContext {

    boolean isRow;
    int index;
    List<String> initialLine;
    List<String> updatedLine;
}
