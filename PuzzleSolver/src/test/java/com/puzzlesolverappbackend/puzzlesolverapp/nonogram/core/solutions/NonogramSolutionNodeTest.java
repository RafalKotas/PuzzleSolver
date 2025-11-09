package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogicFactory;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramSolution;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NonogramSolutionNodeTest {

    NonogramSolutionNode subject;

    @Mock
    NonogramLogicFactory nonogramLogicFactory;

    @Test
    @DisplayName("Should create NonogramSolutionNode - AllArgsConstructor")
    void shouldCreateNonogramSolutionNodeAllArgsConstructor() {
        // given
        NonogramLogic nonogramLogic = create_o06005_logic();
        NonogramSolution nonogramSolution = new NonogramSolution();
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("O", "O")),
                new ArrayList<>(List.of("O", "X"))
        ));
        nonogramSolution.setNonogramBoard(board);

        // when
        subject = new NonogramSolutionNode(nonogramLogic,
                nonogramLogic,
                nonogramSolution,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        // then
        assertThat(subject.getNonogramLogic()).isNotNull();
        assertThat(subject.getNonogramSolution()).isNotNull();
        assertThat(subject.getFullSolutionBoard()).isNotNull();
        assertThat(subject.getNonogramGuessDecisions()).isNotNull().isEmpty();
        assertThat(subject.getNonogramRecursionDecisions()).isNotNull().isEmpty();
        assertThat(subject.getNodeLogs()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should create NonogramSolutionNode and get Fields")
    void shouldCreateNonogramSolutionNodeAllArgsConstructorAndGetFields() {
        // given
        NonogramLogic nonogramLogic = create_o06005_logic();
        when(nonogramLogicFactory.copy(any())).thenAnswer(inv -> inv.getArgument(0));

        // when
        subject = new NonogramSolutionNode(nonogramLogic, nonogramLogicFactory);

        // then
        assertThat(subject.getNonogramLogic()).isNotNull();
        assertThat(subject.getNonogramSolution()).isNotNull();
        assertThat(subject.getFullSolutionBoard()).isNull(); // TODO(?) - remove unused field
        assertThat(subject.getNonogramGuessDecisions()).isNotNull().isEmpty();
        assertThat(subject.getNonogramRecursionDecisions()).isNotNull().isEmpty();
        assertThat(subject.getNodeLogs()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should create NonogramSolutionNode and check toString()")
    void shouldCreateNonogramSolutionNodeAndCheckToString() {
        // given
        NonogramLogic nonogramLogic = create_o06005_logic();
        when(nonogramLogicFactory.copy(any())).thenAnswer(inv -> inv.getArgument(0));
        subject = new NonogramSolutionNode(nonogramLogic, nonogramLogicFactory);

        // when
        String subjectToString = subject.toString();

        // then
        assertThat(subjectToString)
                .contains("NonogramSolutionNode{",
                        "nonogramLogic=",
                        ", nonogramSolution=",
                        ", nonogramGuessDecisions=",
                        ", nonogramRecursionDecisions=",
                        ", nodeLogs=");
    }

    @Test
    @DisplayName("colourOrPlaceX - should place X at given position and add action dependencies")
    void shouldPlaceXAtGivenPositionAndAddActionDependencies() {
        // given
        NonogramLogic logicMock = mock(NonogramLogic.class);
        when(nonogramLogicFactory.copy(any())).thenReturn(logicMock);

        subject = new NonogramSolutionNode(logicMock, nonogramLogicFactory);
        subject.addDecision(new NonogramSolutionDecision("X", new Field(0, 0)));

        // when
        subject.colourOrPlaceX();

        // then
        verify(logicMock, times(1)).placeXAtGivenPosition(any());
        verify(logicMock, times(1)).addAffectedRowAndColumnAfterPlacingXAtField(any());
        verify(logicMock, never()).colourFieldAtGivenPosition(any());
    }

    @Test
    @DisplayName("colourOrPlaceX - should colour at given position and add action dependencies")
    void shouldColourAtGivenPositionAndAddActionDependencies() {
        // given
        NonogramLogic logicMock = mock(NonogramLogic.class);
        when(nonogramLogicFactory.copy(any())).thenReturn(logicMock);

        subject = new NonogramSolutionNode(logicMock, nonogramLogicFactory);
        subject.addDecision(new NonogramSolutionDecision("O", new Field(1, 1)));

        // when
        subject.colourOrPlaceX();

        // then
        verify(logicMock, times(1)).colourFieldAtGivenPosition(any());
        verify(logicMock, times(1)).addAffectedRowAndColumnAfterColouringField(any());
        verify(logicMock, never()).placeXAtGivenPosition(any());
    }

    NonogramLogic create_o06005_logic() {
        List<List<Integer>> rowSequencesLengths = List.of(
                List.of(1, 1, 1),
                List.of(1, 1, 1, 1, 1),
                List.of(1, 5, 1),
                List.of(9),
                List.of(9),
                List.of(7, 1),
                List.of(9),
                List.of(10),
                List.of(1, 1),
                List.of(2, 2)
        );

        List<List<Integer>> columnSequencesLengths = List.of(
                List.of(7),
                List.of(7),
                List.of(8, 1),
                List.of(6),
                List.of(8),
                List.of(6),
                List.of(10),
                List.of(2, 2, 1),
                List.of(7),
                List.of(1)
        );

        NonogramRules rules = new NonogramRules(rowSequencesLengths, columnSequencesLengths, 10, 10);
        return new NonogramLogic(rules, GuessMode.DISABLED);
    }
}