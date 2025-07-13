package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramState;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramActionScheduler;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class MarkOperationContextTest {

    @Test
    void shouldCreateContextWithAllFields() {
        // given
        NonogramActionScheduler scheduler = mock(NonogramActionScheduler.class);
        NonogramState state = mock(NonogramState.class);
        Runnable runnable = () -> {};
        Consumer<String> logConsumer = s -> {};

        // when
        MarkOperationContext context = new MarkOperationContext(scheduler, state, runnable, logConsumer);

        // then
        assertEquals(scheduler, context.getScheduler());
        assertEquals(state, context.getState());
        assertEquals(runnable, context.getAddLogRunnable());
        assertEquals(logConsumer, context.getSetTmpLogConsumer());
    }
}