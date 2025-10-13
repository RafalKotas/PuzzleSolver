package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.utils;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.NonogramRowLogic;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.List;

class NonogramBoardUtilsTest {

    NonogramRowLogic nonogramRowLogic;

    @BeforeEach
    void setUp() {
        nonogramRowLogic = generate_nonogram_o07940_logic();
        nonogramRowLogic.setNonogramSolutionBoard(List.of(
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-"),
                List.of("-", "-", "O", "O", "-", // 0  1  2  3  4
                        "-", "-", "O", "-", "-",          //  5  6  7  8  9
                        "-", "-", "-", "-", "-",          // 10 11 12 13 14
                        "-", "O", "O", "-", "X"),         // 15 16 17 18 19
                Collections.nCopies(20, "-"),
                Collections.nCopies(20, "-")
        ));
    }

    NonogramRowLogic generate_nonogram_o07940_logic() {
        NonogramRowLogic nonogramRowLogic = new NonogramRowLogic();
        NonogramRules nonogramRules = new NonogramRules();
        nonogramRules.setRowSequencesLengths(
                List.of(
                        List.of(3, 2, 1, 2),
                        List.of(6, 9),
                        List.of(6, 7),
                        List.of(2, 2, 1, 2),
                        List.of(4, 10),
                        List.of(3, 2, 2),
                        List.of(5),
                        List.of(3),
                        List.of(2, 1),
                        List.of(6, 3),
                        List.of(3, 1, 2),
                        List.of(5, 1, 1, 1),
                        List.of(8, 2, 3),
                        List.of(8, 2, 1),
                        List.of(6, 2, 1),
                        List.of(5, 3, 2, 2),
                        List.of(5, 2, 3, 4),
                        List.of(4, 1, 2, 3),
                        List.of(3, 1, 4, 2),
                        List.of(2, 4)
                )
        );
        return nonogramRowLogic;
    }
}