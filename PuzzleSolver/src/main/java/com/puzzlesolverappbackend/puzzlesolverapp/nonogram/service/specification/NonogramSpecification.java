package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.specification;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramFilterRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class NonogramSpecification {

    public static Specification<Nonogram> withFilters(NonogramFilterRequest filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.getSources() != null && !filters.getSources().isEmpty()) {
                predicates.add(root.get("source").in(filters.getSources()));
            }
            if (filters.getYears() != null && !filters.getYears().isEmpty()) {
                predicates.add(root.get("year").in(filters.getYears()));
            }
            if (filters.getMonths() != null && !filters.getMonths().isEmpty()) {
                predicates.add(root.get("month").in(filters.getMonths()));
            }
            if (filters.getMinDifficulty() != null && filters.getMaxDifficulty() != null) {
                predicates.add(cb.between(root.get("difficulty"), filters.getMinDifficulty(), filters.getMaxDifficulty()));
            }
            if (filters.getMinWidth() != null && filters.getMaxWidth() != null) {
                predicates.add(cb.between(root.get("width"), filters.getMinWidth(), filters.getMaxWidth()));
            }
            if (filters.getMinHeight() != null && filters.getMaxHeight() != null) {
                predicates.add(cb.between(root.get("height"), filters.getMinHeight(), filters.getMaxHeight()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

