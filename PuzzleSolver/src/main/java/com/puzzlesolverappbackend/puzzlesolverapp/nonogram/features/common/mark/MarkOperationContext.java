package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramState;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramActionScheduler;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Consumer;

@AllArgsConstructor
@Getter
public class MarkOperationContext {
    private final NonogramActionScheduler scheduler;
    private final NonogramState state;
    private final Runnable addLogRunnable;
    private final Consumer<String> setTmpLogConsumer;
}

