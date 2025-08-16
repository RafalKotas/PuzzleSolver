package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.saver;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.FinalNonogramSolutionDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolutionJsonFormatterTest {

    @Test
    @DisplayName("SolutionJsonFormatter constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<SolutionJsonFormatter> constructor = SolutionJsonFormatter.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @DisplayName("format should pretty-print full DTO to expected JSON shape")
    @Test
    void format_shouldPrettyPrintDto() {
        // given
        FinalNonogramSolutionDTO dto = new FinalNonogramSolutionDTO();
        dto.setFinalBoard(List.of(
                List.of("X","X","O","X","O","X","O","X","X","X"),
                List.of("O","X","O","X","O","X","O","X","O","X"),
                List.of("O","X","O","O","O","O","O","X","O","X"),
                List.of("O","O","O","O","O","O","O","O","O","X"),
                List.of("O","O","O","O","O","O","O","O","O","X"),
                List.of("O","O","O","O","O","O","O","X","O","X"),
                List.of("O","O","O","O","O","O","O","O","O","X"),
                List.of("O","O","O","O","O","O","O","O","O","O"),
                List.of("X","O","X","X","X","X","O","X","X","X"),
                List.of("X","O","O","X","X","X","O","O","X","X")
        ));

        dto.setDerivedRowRanges(List.of(
                List.of(List.of(2, 2), List.of(4, 4), List.of(6, 6)),
                List.of(List.of(0, 0), List.of(2, 2), List.of(4, 4), List.of(6, 6), List.of(8, 8)),
                List.of(List.of(0, 0), List.of(2, 6), List.of(8, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 6), List.of(8, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 9)),
                List.of(List.of(1, 1), List.of(6, 6)),
                List.of(List.of(1, 2), List.of(6, 7))
        ));

        dto.setDerivedColumnRanges(List.of(
                List.of(List.of(1, 7)),
                List.of(List.of(3, 9)),
                List.of(List.of(0, 7), List.of(9, 9)),
                List.of(List.of(2, 7)),
                List.of(List.of(0, 7)),
                List.of(List.of(2, 7)),
                List.of(List.of(0, 9)),
                List.of(List.of(3, 4), List.of(6, 7), List.of(9, 9)),
                List.of(List.of(1, 7)),
                List.of(List.of(7, 7))
        ));

        dto.setVerifiedAgainstOriginal("PASS");

        // when
        String json = SolutionJsonFormatter.format(dto);

        // then (exact match including spaces and newlines)
        String expected = """
                {
                  "finalBoard" : [
                    ["X", "X", "O", "X", "O", "X", "O", "X", "X", "X"],
                    ["O", "X", "O", "X", "O", "X", "O", "X", "O", "X"],
                    ["O", "X", "O", "O", "O", "O", "O", "X", "O", "X"],
                    ["O", "O", "O", "O", "O", "O", "O", "O", "O", "X"],
                    ["O", "O", "O", "O", "O", "O", "O", "O", "O", "X"],
                    ["O", "O", "O", "O", "O", "O", "O", "X", "O", "X"],
                    ["O", "O", "O", "O", "O", "O", "O", "O", "O", "X"],
                    ["O", "O", "O", "O", "O", "O", "O", "O", "O", "O"],
                    ["X", "O", "X", "X", "X", "X", "O", "X", "X", "X"],
                    ["X", "O", "O", "X", "X", "X", "O", "O", "X", "X"]
                  ],
                  "derivedRowRanges" : [
                    [[2, 2], [4, 4], [6, 6]],
                    [[0, 0], [2, 2], [4, 4], [6, 6], [8, 8]],
                    [[0, 0], [2, 6], [8, 8]],
                    [[0, 8]],
                    [[0, 8]],
                    [[0, 6], [8, 8]],
                    [[0, 8]],
                    [[0, 9]],
                    [[1, 1], [6, 6]],
                    [[1, 2], [6, 7]]
                  ],
                  "derivedColumnRanges" : [
                    [[1, 7]],
                    [[3, 9]],
                    [[0, 7], [9, 9]],
                    [[2, 7]],
                    [[0, 7]],
                    [[2, 7]],
                    [[0, 9]],
                    [[3, 4], [6, 7], [9, 9]],
                    [[1, 7]],
                    [[7, 7]]
                  ],
                  "verifiedAgainstOriginal" : "PASS"
                }""";

        assertEquals(expected, json);
    }
}