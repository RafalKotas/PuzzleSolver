package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.read.ListAppender;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.debug.NonogramPrinter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class LogGroupingPrinterTest {

    private TestLogAppender appender;

    @BeforeEach
    void setup() {
        appender = new LogGroupingPrinterTest.TestLogAppender();
        Logger root = (Logger) LoggerFactory.getLogger(NonogramPrinter.class);
        root.addAppender(appender);
        appender.start();
    }

    @AfterEach
    void tearDown() {
        appender.stop();
    }

    @Test
    @DisplayName("LogGroupingPrinter constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<LogGroupingPrinter> constructor = LogGroupingPrinter.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
    }

    @Test
    @DisplayName("printLogsGroupedByDetectedType should log grouped by type logs")
    void shouldLogGroupedByTypeLogs() {
        // given
        List<String> rawLogs = List.of(
                "EXCLUSION_SEQUENCE_IN_ROW: row=7\n" +
                        "sequenceIndex=0\n" +
                        "line=[O, O, O, O, O, O, O, O, O, O]\n" +
                        "sequencesLengths=[10]\n" +
                        "sequencesRanges=[[0, 9]]\n",
                "FILL_TRIVIAL_SEQUENCE_IN_ROW: row=7\n" +
                        "sequencesLengths=[10]\n" +
                        "sequencesRanges=[[0, 9]]\n" +
                        "initialLine=[-, -, -, -, -, -, -, -, -, -]\n" +
                        "updatedLine=[O, O, O, O, O, O, O, O, O, O]\n",
                "EXCLUSION_SEQUENCE_IN_ROW: row=2\n" +
                        "sequenceIndex=0\n" +
                        "line=[O, O, O, O, O, O, O, O, X, O]\n" +
                        "sequencesLengths=[8, 1]\n" +
                        "sequencesRanges=[[0, 7], [9, 9]]\n",
                "EXCLUSION_SEQUENCE_IN_ROW: row=2\n" +
                        "sequenceIndex=1\n" +
                        "line=[O, O, O, O, O, O, O, O, X, O]\n" +
                        "sequencesLengths=[8, 1]\n" +
                        "sequencesRanges=[[0, 7], [9, 9]]\n",
                "FILL_TRIVIAL_SEQUENCE_IN_COLUMN: column=2\n" +
                        "sequencesLengths=[8, 1]\n" +
                        "sequencesRanges=[[0, 7], [9, 9]]\n" +
                        "initialLine=[-, -, -, -, -, -, -, O, -, -]\n" +
                        "updatedLine=[O, O, O, O, O, O, O, O, X, O]\n"
        );
        List<String> convertedLogs = List.of(
          "Arguments.of(\"o06005 / row=7 - excluding sequence\",\n" +
                  "    0,\n" +
                  "    List.of(\"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\"),\n" +
                  "    List.of(10),\n" +
                  "    List.of(List.of(0, 9)))\n" +
          ")",
          "Arguments.of(\"o06005 / row=7 - trivial row fill\",\n" +
                  "    List.of(List.of(10)),\n" +
                  "    List.of(new ArrayList<>(List.of(new ArrayList<>(List.of(0, 9))))),\n" +
                  "    List.of(new ArrayList<>(List.of(\"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\"))),\n" +
                  "    List.of(new ArrayList<>(List.of(\"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\")))\n" +
          ")",
          "Arguments.of(\"o06005 / row=2 - excluding sequence\",\n" +
                  "    0,\n" +
                  "    List.of(\"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"X\", \"O\"),\n" +
                  "    List.of(8, 1),\n" +
                  "    List.of(List.of(0, 7), List.of(9, 9)))\n" +
          ")",
          "Arguments.of(\"o06005 / row=2 - excluding sequence\",\n" +
                  "    1,\n" +
                  "    List.of(\"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"X\", \"O\"),\n" +
                  "    List.of(8, 1),\n" +
                  "    List.of(List.of(0, 7), List.of(9, 9)))\n" +
          ")",
          "Arguments.of(\"o06005 / column=2 - trivial column fill\",\n" +
                  "    List.of(List.of(8, 1)),\n" +
                  "    List.of(new ArrayList<>(List.of(new ArrayList<>(List.of(0, 7)), new ArrayList<>(List.of(9, 9))))),\n" +
                  "    List.of(new ArrayList<>(List.of(\"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"O\", \"-\", \"-\"))),\n" +
                  "    List.of(new ArrayList<>(List.of(\"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"X\", \"O\")))\n" +
                  ")"
        );
        Logger logger = (Logger) LoggerFactory.getLogger(LogGroupingPrinter.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        Level prev = logger.getLevel();
        logger.setLevel(Level.INFO);
        logger.addAppender(appender);

        // when
        LogGroupingPrinter.printLogsGroupedByDetectedType(rawLogs, convertedLogs);

        // then
        List<String> logs = appender.list.stream().map(ILoggingEvent::getFormattedMessage).toList();

        assertThat(logs).isNotEmpty();
        assertThat(logs.get(0)).contains("-------------EXCLUDED-------------------");
        assertThat(logs.stream().anyMatch(l -> l.contains("Arguments.of(\"o06005 / row=7 - excluding sequence\""))).isTrue();
        assertThat(logs.stream().anyMatch(l -> l.contains("Arguments.of(\"o06005 / row=2 - excluding sequence\""))).isTrue(); // x 2
        assertThat(logs.stream().anyMatch(l -> l.contains("Arguments.of(\"o06005 / row=7 - trivial row fill\""))).isTrue();
        assertThat(logs.stream().anyMatch(l -> l.contains("Arguments.of(\"o06005 / column=2 - trivial column fill\""))).isTrue();
        assertThat(logs.stream().anyMatch(l -> l.contains("-------------TRIVIAL-------------------"))).isTrue();

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