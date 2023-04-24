package com.puzzlesolverappbackend.puzzleAppFileManager.services;

import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramFiltersResponse;
import com.puzzlesolverappbackend.puzzleAppFileManager.repository.NonogramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NonogramService {

    @Autowired
    NonogramRepository nonogramRepository;

    public List<String> getNonogramSources() {
      return nonogramRepository.selectNonogramSources().stream().sorted().collect(Collectors.toList());
    }

    public List<String> getNonogramYears() {
        return nonogramRepository.selectNonogramYears().stream().sorted().collect(Collectors.toList());
    }

    public List<String> getNonogramMonths() {
        return nonogramRepository.selectNonogramMonths().stream().sorted().collect(Collectors.toList());
    }

    public List<Double> getNonogramDifficulties() {
        return nonogramRepository.selectNonogramDifficulties().stream().sorted().collect(Collectors.toList());
    }

    public List<Integer> getNonogramHeights() {
        return nonogramRepository.selectNonogramHeights().stream().sorted().collect(Collectors.toList());
    }

    public List<Integer> getNonogramWidths() {
        return nonogramRepository.selectNonogramWidths().stream().sorted().collect(Collectors.toList());
    }

    public List<Integer> getIntegerBounds(List<Integer> integerList) {
        List<Integer> bounds = new ArrayList<>();
        if(integerList.size() >= 2) {
            bounds.add(integerList.get(0));
            bounds.add(integerList.get(integerList.size() - 1));
        } else {
            bounds.add(5);
            bounds.add(10);
        }

        return bounds;
    }

    /**
     * Creates 2 num range if list contains one element or makes 2 nums list from specified doubles
     * @param  doublesList 2 or 1 element list of doubles to create range from
     * @param  rangeLowerBound specified range lower bound
     * @param  rangeUpperBound specified range upper bound
     * @return range created from doublesList or range specified bounds
     * **/
    public List<Double> getDoubleBounds(List<Double> doublesList, double rangeLowerBound, double rangeUpperBound) {
        List<Double> bounds = new ArrayList<>();
        if(doublesList.size() >= 2) {
            bounds.add(doublesList.get(0));
            bounds.add(doublesList.get(doublesList.size() - 1));
        } else {
            bounds.add(rangeLowerBound);
            bounds.add(rangeUpperBound);
        }

        return bounds;
    }

    public NonogramFiltersResponse getNonogramFilters() {
        List<String> sources = getNonogramSources();
        List<String> years = getNonogramYears();
        List<String> months = getNonogramMonths();
        List<Double> difficulties = getDoubleBounds(getNonogramDifficulties(), 1.0, 2.0);
        List<Integer> heights = getIntegerBounds(getNonogramHeights());
        List<Integer> widths = getIntegerBounds(getNonogramWidths());

        return new NonogramFiltersResponse(sources, years, months, difficulties, heights, widths);
    }
}
