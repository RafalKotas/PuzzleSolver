package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.specification;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramFilterRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class NonogramSpecification {

    public static Specification<Nonogram> withFilters(NonogramFilterRequest filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            addInPredicateIfPresent(filters.getSources().stream().toList(), root.get("source"), predicates);
            Path<Object> publication = root.get("publication");
            addInPredicateIfPresent(filters.getYears().stream().toList(), publication.get("year"), predicates);
            addInPredicateIfPresent(filters.getMonths().stream().toList(), publication.get("month"), predicates);

            addRangePredicateIfPresent(filters.getMinDifficulty(), filters.getMaxDifficulty(), root.get("difficulty"), cb, predicates);
            Path<Object> dimensions = root.get("dimensions");
            addRangePredicateIfPresent(filters.getMinWidth(), filters.getMaxWidth(), dimensions.get("width"), cb, predicates);
            addRangePredicateIfPresent(filters.getMinHeight(), filters.getMaxHeight(), dimensions.get("height"), cb, predicates);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static <T> void addInPredicateIfPresent(List<T> values, Path<T> path, List<Predicate> predicates) {
        if (values != null && !values.isEmpty()) {
            predicates.add(path.in(values));
        }
    }

    private static <N extends Number & Comparable<N>> void addRangePredicateIfPresent(
            N min, N max, Path<N> path, CriteriaBuilder cb, List<Predicate> predicates) {
        if (min != null && max != null) {
            predicates.add(cb.between(path, min, max));
        }
    }
}
