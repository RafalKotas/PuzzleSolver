package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class MarkContextTest {

    @Test
    void shouldHoldAllProvidedContexts() {
        // given
        BoardContext boardContext = mock(BoardContext.class);
        SequencesContext sequencesContext = mock(SequencesContext.class);
        MarkOperationContext operationContext = mock(MarkOperationContext.class);

        // when
        MarkContext context = new MarkContext(boardContext, sequencesContext, operationContext);

        // then
        assertEquals(boardContext, context.getBoard());
        assertEquals(sequencesContext, context.getSequences());
        assertEquals(operationContext, context.getOps());
    }
}
