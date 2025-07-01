package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.LogConverter.detectActionTypeFromRawLog;

@UtilityClass
public class LogGroupingPrinter {

    public static void printLogsGroupedByDetectedType(List<String> rawLogs, List<String> convertedLogs) {
        Map<String, List<String>> grouped = new LinkedHashMap<>();

        for (int i = 0; i < rawLogs.size(); i++) {
            String rawLog = rawLogs.get(i);
            String converted = convertedLogs.get(i);
            String actionType = detectActionTypeFromRawLog(rawLog);

            grouped.computeIfAbsent(actionType, k -> new ArrayList<>()).add(converted);
        }

        for (Map.Entry<String, List<String>> entry : grouped.entrySet()) {
            String action = entry.getKey();
            List<String> logs = entry.getValue();

            System.out.println("-------------" + action + "-------------------");
            logs.forEach(System.out::println);
            System.out.println();
        }
    }
}

