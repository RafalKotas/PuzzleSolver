package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class NonogramLogicTest {

    private NonogramLogic nonogramLogic_o10355;

    @BeforeEach
    void initNonogram10355() {
        //given
        List<List<Integer>> rowsSequences = List.of(
                List.of(6), List.of(7, 2), List.of(7, 1), List.of(2, 1, 4, 1), List.of(2, 1, 1, 2, 1),
                List.of(3, 3, 1, 2, 1), List.of(7, 1, 1, 2, 2), List.of(7, 8), List.of(17), List.of(3, 2, 1),
                List.of(4, 1, 1, 1, 1, 5), List.of(18), List.of(2, 9, 4), List.of(1, 2, 1, 2), List.of(2, 2)
        );
        List<List<Integer>> columnsSequences = List.of(
                List.of(4), List.of(1, 9), List.of(10, 1), List.of(7, 2, 2), List.of(1, 3, 3),
                List.of(1, 4, 3), List.of(1, 4, 2), List.of(8, 3), List.of(1, 1, 2), List.of(1, 6, 3),
                List.of(1, 1, 2, 2), List.of(1, 1, 3, 3), List.of(1, 2, 2, 3), List.of(2, 2, 2, 2, 1), List.of(1, 7, 2),
                List.of(1, 8), List.of(1, 2, 3), List.of(1, 5), List.of(2, 1, 1), List.of(6)
        );

        nonogramLogic_o10355 = new NonogramLogic(rowsSequences, columnsSequences, false);
    }

    @Test
    void shouldGenerateProperSequencesRanges() {

        //then
        assertThat(nonogramLogic_o10355.getRowsSequencesRanges()).isEqualTo(
                List.of(
                        List.of(List.of(0, 19)), // [0, 19]
                        List.of(List.of(0, 16), List.of(8, 19)),
                        List.of(List.of(0, 17), List.of(8, 19)),
                        List.of(List.of(0, 10), List.of(3, 12), List.of(5, 17), List.of(10, 19)),
                        List.of(List.of(0, 10), List.of(3, 12), List.of(5, 14), List.of(7, 17), List.of(10, 19)),

                        List.of(List.of(0, 8), List.of(4, 12), List.of(8, 14), List.of(10, 17), List.of(13, 19)),
                        List.of(List.of(0, 9), List.of(8, 11), List.of(10, 13), List.of(12, 16), List.of(15, 19)),
                        List.of(List.of(0, 10), List.of(8, 19)),
                        List.of(List.of(0, 19)),
                        List.of(List.of(0, 14), List.of(4, 17), List.of(7, 19)),

                        List.of(List.of(0, 5), List.of(5, 7), List.of(7, 9), List.of(9, 11), List.of(11, 13), List.of(13, 19)),
                        List.of(List.of(0, 19)),
                        List.of(List.of(0, 4), List.of(3, 14), List.of(13, 19)),
                        List.of(List.of(0, 11), List.of(2, 14), List.of(5, 16), List.of(7, 19)),
                        List.of(List.of(0, 16), List.of(3, 19))
                )
        );
    }
}