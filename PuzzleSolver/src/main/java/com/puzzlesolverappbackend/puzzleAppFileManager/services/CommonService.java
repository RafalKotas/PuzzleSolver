package com.puzzlesolverappbackend.puzzleAppFileManager.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.Collectors;

@Service
public class CommonService {

    public Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }
}
