package com.puzzlesolverappbackend.puzzleAppFileManager.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class CommonService {

    public Set<String> listFilesUsingJavaIO(String dir) {
        log.info("Start loading files from directory: {}", dir);
        try {
            return Stream.of(new File(dir).listFiles())
                    .filter(file -> !file.isDirectory())
                    .map(File::getName)
                    .collect(Collectors.toSet());
        } catch (NullPointerException exception) {
            return Collections.emptySet();
        }
    }
}
