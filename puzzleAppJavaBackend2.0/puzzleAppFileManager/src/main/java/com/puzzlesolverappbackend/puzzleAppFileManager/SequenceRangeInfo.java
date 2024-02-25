package com.puzzlesolverappbackend.puzzleAppFileManager;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SequenceRangeInfo {

    List<Integer> range;
    int rowOrColumnNo;
    int seqNo;
}
