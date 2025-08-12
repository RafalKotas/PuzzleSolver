package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogConverter.detectActionTypeFromRawLog;

@UtilityClass
@Slf4j
public class LogGroupingPrinter {

    public static void printLogsGroupedByDetectedType(List<String> rawLogs, List<String> convertedLogs) {
        Map<String, List<String>> grouped = new LinkedHashMap<>();

        for (int i = 0; i < rawLogs.size(); i++) {
            String rawLog = rawLogs.get(i);
            if (i >= convertedLogs.size()) {
                System.out.println("tu się wyjebie");
            }
            String converted = convertedLogs.get(i);
            String actionType = detectActionTypeFromRawLog(rawLog);

            grouped.computeIfAbsent(actionType, k -> new ArrayList<>()).add(converted);
        }

        for (Map.Entry<String, List<String>> entry : grouped.entrySet()) {
            String action = entry.getKey();
            List<String> logs = entry.getValue();

            log.info("-------------{}-------------------", action);
            for (String s : logs) {
                log.info("{}", s);
            }
            log.info("");
        }
    }
}

