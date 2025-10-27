package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NonogramRowLogicTest {

    private NonogramRowLogic subject;

    @Test
    @DisplayName("preventExtendingColouredSequenceToExcessLengthInRow - should place X before sequence and correct range")
    void shouldPreventOverextensionToLeft() {
        // given
        NonogramLogic nonogramLogic = create_o10401_NonogramLogic();
        subject = nonogramLogic.getNonogramRowLogic();

        int rowIdx = 8;
        List<List<Integer>> row8sequencesRanges = new ArrayList<>(
               List.of(
                       new ArrayList<>(List.of(0, 13)),
                       new ArrayList<>(List.of(2, 18)),
                       new ArrayList<>(List.of(13, 27)),
                       new ArrayList<>(List.of(18, 29)),
                       new ArrayList<>(List.of(25, 33)),
                       new ArrayList<>(List.of(37, 39))
               )
        );
        List<String> boardRow8 = new ArrayList<>(
                List.of("-", "-", "-", "-", "-",
                        "-", "-", "-", "-", "-",
                        "-", "-", "-", "-", "O",
                        "O", "-", "-", "O", "X",
                        "X", "X", "X", "X", "X",
                        "O", "O", "O", "X", "-",
                        "X", "-", "-", "-", "X",
                        "X", "X", "O", "O", "O"
                )
        );
        subject.getNonogramSolutionBoard().set(8, boardRow8);
        subject.setRowSequencesRanges(8, row8sequencesRanges);


        subject.preventExtendingColouredSequenceToExcessLengthInRow(rowIdx);

        // then
        assertThat(subject.getNonogramSolutionBoard().get(rowIdx).get(17))
                .as("Expected X before coloured sequence start (col 18)")
                .isEqualTo("X");

        List<List<Integer>> after = subject.getRowsSequencesRanges().get(rowIdx);
        assertThat(after.get(3))
                .as("Expected updated range from [18, 29] to [18, 18]")
                .isEqualTo(List.of(18, 18));

        assertThat(nonogramLogic.getLogs())
                .anyMatch(log -> log.contains("PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH"));
    }

    private NonogramLogic create_o10401_NonogramLogic() {
        NonogramRules rules = new NonogramRules();
        rules.setHeight(30);
        rules.setWidth(40);

        rules.setRowSequencesLengths(List.of(
                List.of(9), List.of(5, 5), List.of(5, 1, 5), List.of(6, 1, 6), List.of(8, 7, 7),
                List.of(2, 2, 15), List.of(2, 6, 2, 5, 5), List.of(2, 4, 3, 2, 4, 4), List.of(1, 4, 3, 1, 3, 3), List.of(2, 3, 2, 2, 2, 4, 3, 2),
                List.of(1, 2, 2, 1, 1, 1, 1, 1, 1), List.of(1, 1, 4, 3, 1, 1, 1, 1, 1, 1), List.of(2, 1, 1, 1, 1, 1, 2, 2, 3, 2), List.of(1, 1, 1, 1, 1, 1, 1, 1, 1), List.of(1, 2, 2, 2, 1, 2, 3, 2),
                List.of(2, 2, 1, 1, 3, 2, 2, 2), List.of(1, 4, 2, 4, 1, 4, 2), List.of(2, 5, 5, 1, 1, 1), List.of(1, 7, 6, 2, 5), List.of(1, 6, 6, 1, 11),
                List.of(2, 4, 2, 4, 1, 5, 1, 5), List.of(1, 2, 1, 3, 8, 1, 4), List.of(1, 2, 1, 3, 1, 4, 1, 4), List.of(1, 3, 2, 2, 2, 1, 4, 1, 4), List.of(8, 1, 1, 1, 2, 4, 1, 4),
                List.of(11, 6, 1, 4, 1, 4), List.of(12, 6, 1, 4, 1, 4), List.of(2, 7, 3, 2, 1, 4, 1, 4), List.of(1, 6, 2, 1, 1, 4, 1, 4), List.of(1, 6, 2, 1, 1, 4, 1, 4)
        ));
        rules.setColumnSequencesLengths(List.of(
                List.of(3), List.of(4, 4), List.of(3, 4), List.of(4, 5), List.of(4, 4, 3),
                List.of(3, 6, 4), List.of(2, 11, 7), List.of(2, 3, 7, 7), List.of(2, 3, 1, 4, 7), List.of(1, 4, 2, 3, 6),
                List.of(1, 3, 1, 1, 1, 5), List.of(1, 2, 4, 1, 1, 5), List.of(1, 1, 1, 1, 4), List.of(1, 2, 1, 1, 4), List.of(1, 3, 2, 4),
                List.of(2, 3, 1, 4, 3), List.of(2, 3, 7, 7), List.of(2, 14, 3), List.of(3, 8, 2), List.of(4, 6, 2),
                List.of(4, 5), List.of(4, 5), List.of(4), List.of(4), List.of(1, 6),
                List.of(7, 2), List.of(11, 10), List.of(8, 3, 11), List.of(8, 1, 2, 11), List.of(7, 2, 2, 11),
                List.of(6, 1, 1, 4), List.of(2, 2, 4, 1, 1, 2), List.of(1, 2, 1, 1, 1, 1, 12), List.of(2, 2, 2, 1, 2), List.of(6, 1, 1, 4),
		        List.of(7, 2, 2, 11), List.of(8, 1, 2, 11), List.of(8, 3, 11), List.of(11, 10), List.of(7, 1)
        ));

        return new NonogramLogic(rules, GuessMode.DISABLED);
    }

}