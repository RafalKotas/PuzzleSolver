package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
@Slf4j
public class NonogramLogService {

    private final List<String> logs = new ArrayList<>();

    private String tmpLog = "";

    public void addLog() {
        if (tmpLog == null || tmpLog.isEmpty()) {
            log.warn("Trying to add empty log!!!");
        } else {
            logs.add(tmpLog);
            tmpLog = ""; // Clear after adding
        }
    }

    public List<String> getLogs() {
        return Collections.unmodifiableList(logs);
    }

    public void clear() {
        logs.clear();
        tmpLog = "";
    }
}

