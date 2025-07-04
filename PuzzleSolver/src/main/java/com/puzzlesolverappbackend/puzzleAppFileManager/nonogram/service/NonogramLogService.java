package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.service;

import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
public class NonogramLogService {

    private final List<String> logs = new ArrayList<>();

    private String tmpLog = "";

    public void addLog() {
        if (tmpLog == null || tmpLog.isEmpty()) {
            System.out.println("Trying to add empty log!!!");
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

