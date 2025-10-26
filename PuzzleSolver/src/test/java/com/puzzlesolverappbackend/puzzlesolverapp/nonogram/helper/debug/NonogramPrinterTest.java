package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.debug;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.read.ListAppender;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class NonogramPrinterTest {

    NonogramPrinter subject;

    private TestLogAppender appender;

    @BeforeEach
    void setup() {
        appender = new TestLogAppender();
        Logger root = (Logger) LoggerFactory.getLogger(NonogramPrinter.class);
        root.addAppender(appender);
        appender.start();
    }

    @AfterEach
    void tearDown() {
        appender.stop();
    }

    @DisplayName("NonogramPrinter keeps provided NonogramLogic")
    @Test
    void printerKeepsLogicInstance() {
        // given
        NonogramLogic logic = buildLogic_o06005_withBoardAndRanges();

        // when
        subject = new NonogramPrinter(logic);

        // then
        assertThat(subject.getLogic()).isSameAs(logic);
    }

    private NonogramLogic buildLogic_o06005_withBoardAndRanges() {
        // given – rules from o06005
        List<List<Integer>> rowSequences = List.of(
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

        List<List<Integer>> colSequences = List.of(
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

        NonogramRules rules = new NonogramRules(rowSequences, colSequences, 10, 10);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        // and – board (mutable)
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"))
        ));
        logic.setNonogramSolutionBoard(board);

        // and – set explicit column 0 ranges [[0, 9]]
        List<List<List<Integer>>> colRanges = logic.getColumnsSequencesRanges();
        colRanges.set(0, new ArrayList<>(List.of(new ArrayList<>(List.of(0, 9)))));

        // and – add the overlap log entry we want to see
        String overlapLog = """
                OVERLAP_COLUMN_SEQUENCE: col=0
                initial=[-, -, -, -, -, -, -, O, -, -]
                ranges=[[0, 9]]
                lengths=[7]
                final=[-, -, -, O, O, O, O, O, -, -]
                """;
        logic.getLogs().add(overlapLog);

        return logic;
    }

    @Test
    @DisplayName("NonogramPrinter: print* should log expected number of lines (smoke, o06005)")
    void smokePrintCalls_o06005() {
        // given
        NonogramLogic logic = buildLogic_o06005_withBoardAndRanges();
        subject = new NonogramPrinter(logic);

        // when
        subject.printNonogramBoard();
        subject.printNonogramBoardWithMarks();
        subject.printRowsSequencesRanges();
        subject.printColumnsSequencesRanges();
        subject.printLogs();
        subject.printStats();

        // then
        assertThat(appender.getLogs()).isNotEmpty();
        // minimal sanity check — at least as many logs as board rows
        assertThat(appender.getLogs().size()).isGreaterThanOrEqualTo(logic.getNonogramRules().getHeight());

        // optional — check a specific expected substring
        assertThat(appender.getLogs()).anyMatch(line -> line.contains("Nonogram board:"));
        assertThat(appender.getLogs()).anyMatch(line -> line.contains("completion percentage"));
    }

    @DisplayName("NonogramPrinter: printNonogramBoard logs one line per row with index prefix (o06005)")
    @Test
    void printNonogramBoard_shouldLogRows_o06005() {
        // given
        NonogramLogic logic = buildLogic_o06005_withBoardAndRanges();
        subject = new NonogramPrinter(logic);

        Logger logger = (Logger) LoggerFactory.getLogger(NonogramPrinter.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        Level prev = logger.getLevel();
        logger.setLevel(Level.INFO);
        logger.addAppender(appender);

        // when
        subject.printNonogramBoard();

        // then
        List<ILoggingEvent> events = appender.list;
        int h = logic.getNonogramRules().getHeight();
        assertThat(events).hasSize(h);

        String first = events.get(0).getFormattedMessage();
        String last = events.get(h - 1).getFormattedMessage();

        assertThat(first).startsWith(String.format("%3d ", 0));
        assertThat(last).startsWith(String.format("%3d ", h - 1));
        assertThat(first).contains("[");

        // cleanup
        logger.detachAppender(appender);
        logger.setLevel(prev);
        appender.stop();
    }

    @DisplayName("NonogramPrinter: printRows/Columns ranges and printLogs log meaningful content (o06005)")
    @Test
    void shouldLogRangesAndOverlapLog_o06005() {
        // given
        NonogramLogic logic = buildLogic_o06005_withBoardAndRanges();
        subject = new NonogramPrinter(logic);

        Logger logger = (Logger) LoggerFactory.getLogger(NonogramPrinter.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        Level prev = logger.getLevel();
        logger.setLevel(Level.INFO);
        logger.addAppender(appender);

        // when
        subject.printRowsSequencesRanges();
        subject.printColumnsSequencesRanges();
        subject.printLogs();

        // then
        List<String> lines = appender.list.stream().map(ILoggingEvent::getFormattedMessage).toList();
        String all = String.join("\n", lines);

        assertThat(all).contains("  0 ");
        assertThat(all).contains("[[0, 9]]");

        assertThat(all).contains("OVERLAP_COLUMN_SEQUENCE: col=0");
        assertThat(all).contains("initial=[-, -, -, -, -, -, -, O, -, -]");
        assertThat(all).contains("final=[-, -, -, O, O, O, O, O, -, -]");

        // cleanup
        logger.detachAppender(appender);
        logger.setLevel(prev);
        appender.stop();
    }

    static class TestLogAppender extends AppenderBase<ILoggingEvent> {
        private final List<String> logs = new ArrayList<>();

        @Override
        protected void append(ILoggingEvent eventObject) {
            logs.add(eventObject.getFormattedMessage());
        }

        List<String> getLogs() {
            return logs;
        }
    }
}