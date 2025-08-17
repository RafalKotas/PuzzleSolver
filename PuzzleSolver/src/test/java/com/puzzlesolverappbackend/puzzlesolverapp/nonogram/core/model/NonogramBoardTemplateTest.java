package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.exception.NonogramFileReadException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.constants.SharedConstants.JSON_EXTENSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NonogramBoardTemplateTest {

    private Path solutionsDir() {
        return Paths.get(InitializerConstants.NONOGRAM_SOLUTIONS_PATH);
    }

    @Test
    @DisplayName("Constructor loads board from JSON file")
    void constructorLoadsBoardSuccess() throws Exception {
        // given
        Files.createDirectories(solutionsDir());
        String fname = "nbt_success_" + System.nanoTime();
        Path file = solutionsDir().resolve(fname + JSON_EXTENSION);
        String json = """
            {"board":[
              ["X","-","O"],
              ["-","O","X"]
            ]}
            """;
        Files.writeString(file, json);
        file.toFile().deleteOnExit();

        // when
        NonogramBoardTemplate nonogramBoardTemplate = new NonogramBoardTemplate(fname);
        nonogramBoardTemplate.setBoard(new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of("X", "O", "X")),
                        new ArrayList<>(List.of("X", "O", "X"))
                )
        ));

        // then
        assertThat(nonogramBoardTemplate.getBoard())
                .containsExactly(
                        List.of("X","O","X"),
                        List.of("X","O","X")
                );
    }

    @Test
    @DisplayName("Constructor throws when file missing")
    void constructorMissingFileThrowsException() {
        // given
        String notExisting = "nbt_missing_" + System.nanoTime();

        // when / then
        assertThatThrownBy(() -> new NonogramBoardTemplate(notExisting))
                .isInstanceOf(NonogramFileReadException.class)
                .hasMessageContaining("Could not find nonogram solution file")
                .hasMessageContaining(notExisting);
    }

    @Test
    @DisplayName("printBoard() logs each row when board is not empty")
    void printBoardLogsRowsWhenNotEmpty() throws Exception {
        // given
        Files.createDirectories(solutionsDir());
        String fname = "nbt_log_" + System.nanoTime();
        Path file = solutionsDir().resolve(fname + JSON_EXTENSION);
        String json = """
            {"board":[
              ["X","-","O"],
              ["-","O","X"]
            ]}
            """;
        Files.writeString(file, json);
        file.toFile().deleteOnExit();

        NonogramBoardTemplate tpl = new NonogramBoardTemplate(fname);

        Logger logger = (Logger) LoggerFactory.getLogger(NonogramBoardTemplate.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);

        // when
        tpl.printBoard();

        // then
        assertThat(appender.list).hasSize(2);
        assertThat(appender.list.get(0).getFormattedMessage()).isEqualTo("[X, -, O]");
        assertThat(appender.list.get(1).getFormattedMessage()).isEqualTo("[-, O, X]");

        logger.detachAppender(appender);
        appender.stop();
    }

    @Test
    @DisplayName("printBoard() logs nothing when board is empty")
    void printBoardNoLogsWhenEmpty() throws Exception {
        // given
        Files.createDirectories(solutionsDir());
        String fname = "nbt_empty_" + System.nanoTime();
        Path file = solutionsDir().resolve(fname + JSON_EXTENSION);
        String json = """
            {"board":[]}
            """;
        Files.writeString(file, json);
        file.toFile().deleteOnExit();

        NonogramBoardTemplate tpl = new NonogramBoardTemplate(fname);

        Logger logger = (Logger) LoggerFactory.getLogger(NonogramBoardTemplate.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);

        // when
        tpl.printBoard();

        // then
        assertThat(appender.list).isEmpty();

        logger.detachAppender(appender);
        appender.stop();
    }
}
