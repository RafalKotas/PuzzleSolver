package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NonogramFilterRequestTest {

    @Test
    @DisplayName("should correctly construct NonogramFilterRequest and return values")
    void shouldConstructNonogramFilterRequest() {
        // given
        List<String> sources = List.of("user", "system");
        List<String> years = List.of("2024");
        List<String> months = List.of("07");
        Double minDifficulty = 1.0;
        Double maxDifficulty = 5.0;
        Integer minHeight = 5;
        Integer maxHeight = 20;
        Integer minWidth = 5;
        Integer maxWidth = 20;

        // when
        NonogramFilterRequest request = new NonogramFilterRequest(
                sources, years, months,
                minDifficulty, maxDifficulty,
                minHeight, maxHeight, minWidth, maxWidth
        );

        // then
        assertEquals(List.of("user", "system"), request.getSources());
        assertEquals(List.of("2024"), request.getYears());
        assertEquals(List.of("07"), request.getMonths());
        assertEquals(1.0, request.getMinDifficulty());
        assertEquals(5.0, request.getMaxDifficulty());
        assertEquals(5, request.getMinHeight());
        assertEquals(20, request.getMaxHeight());
        assertEquals(5, request.getMinWidth());
        assertEquals(20, request.getMaxWidth());
    }

    @Test
    @DisplayName("NoArgsConstructor + setters should set values and getters should return them")
    void noArgsConstructor_andSetters_shouldSetAndGetValues() {
        // given
        NonogramFilterRequest req = new NonogramFilterRequest(); // no-args ctor

        // when
        req.setSources(List.of("user", "system"));
        req.setYears(Set.of("2024"));
        req.setMonths(List.of("07"));
        req.setMinDifficulty(1.0);
        req.setMaxDifficulty(5.0);
        req.setMinHeight(5);
        req.setMaxHeight(20);
        req.setMinWidth(5);
        req.setMaxWidth(20);

        // then
        assertThat(req.getSources()).containsExactly("user", "system");
        assertThat(req.getYears()).containsExactly("2024");
        assertThat(req.getMonths()).containsExactly("07");
        assertThat(req.getMinDifficulty()).isEqualTo(1.0);
        assertThat(req.getMaxDifficulty()).isEqualTo(5.0);
        assertThat(req.getMinHeight()).isEqualTo(5);
        assertThat(req.getMaxHeight()).isEqualTo(20);
        assertThat(req.getMinWidth()).isEqualTo(5);
        assertThat(req.getMaxWidth()).isEqualTo(20);
    }

    @Test
    @DisplayName("equals and hashCode should work correctly for identical and different objects")
    void equalsAndHashCode_shouldWorkCorrectly() {
        // given
        NonogramFilterRequest r1 = new NonogramFilterRequest(
                List.of("src1"), List.of("2024"), List.of("07"),
                1.0, 5.0,
                10, 20, 10, 20
        );

        NonogramFilterRequest r2 = new NonogramFilterRequest(
                List.of("src1"), List.of("2024"), List.of("07"),
                1.0, 5.0,
                10, 20, 10, 20
        );

        NonogramFilterRequest r3 = new NonogramFilterRequest(
                List.of("src2"), List.of("2024"), List.of("07"),
                1.0, 5.0,
                10, 20, 10, 20
        );

        // then
        assertThat(r1)
                .isEqualTo(r2)
                .hasSameHashCodeAs(r2);

        assertThat(r1)
                .isNotEqualTo(r3);

        assertThat(r1.equals(null)).isFalse();
        assertThat(r1.equals("someString")).isFalse();
    }

    @Test
    @DisplayName("equals: two objects with identical values (including lists) are equal and have same hashCode")
    void equals_identicalObjects_haveSameHashCode() {
        // given
        NonogramFilterRequest a = new NonogramFilterRequest(
                List.of("s"), List.of("2024"), List.of("07"),
                1.0, 5.0,
                10, 20, 5, 15
        );
        NonogramFilterRequest b = new NonogramFilterRequest(
                List.of("s"), List.of("2024"), List.of("07"),
                1.0, 5.0,
                10, 20, 5, 15
        );

        // when & then
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
        // symmetry
        assertThat(b).isEqualTo(a);
    }

    @Test
    @DisplayName("equals: objects with null fields should be equal when nulls are in same places")
    void equals_withNullFields_equalWhenBothNull() {
        // given & when
        NonogramFilterRequest a = new NonogramFilterRequest(
                null, null, null,
                null, null,
                null, null, null, null
        );
        NonogramFilterRequest b = new NonogramFilterRequest(
                null, null, null,
                null, null,
                null, null, null, null
        );

        // then
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("equals: not equal when one collection is List and other is Set even if contents are same")
    void equals_listVsSet_notEqual() {
        // given
        NonogramFilterRequest listRequest = new NonogramFilterRequest(
                List.of("A"), List.of("2024"), List.of("07"),
                1.0, 2.0, 5, 10, 5, 10
        );
        NonogramFilterRequest setRequest = new NonogramFilterRequest();

        // when
        setRequest.setSources(Set.of("A"));
        setRequest.setYears(List.of("2024"));
        setRequest.setMonths(List.of("07"));
        setRequest.setMinDifficulty(1.0);
        setRequest.setMaxDifficulty(2.0);
        setRequest.setMinHeight(5);
        setRequest.setMaxHeight(10);
        setRequest.setMinWidth(5);
        setRequest.setMaxWidth(10);

        // then
        assertThat(listRequest).isNotEqualTo(setRequest);
    }

    @Test
    @DisplayName("equals: not equal when a single field differs")
    void equals_notEqualWhenOneFieldDiffers() {
        // given
        NonogramFilterRequest base = new NonogramFilterRequest(
                List.of("s"), List.of("2024"), List.of("07"),
                1.0, 5.0,
                10, 20, 5, 15
        );

        // when
        NonogramFilterRequest diff = new NonogramFilterRequest(
                List.of("s"), List.of("2024"), List.of("07"),
                1.0, 5.0,
                11, 20, 5, 15 // minHeight differs
        );

        // then
        assertThat(base).isNotEqualTo(diff);
    }

    @Test
    @DisplayName("hashCode: equal objects including null fields produce same hash code")
    void hashCode_equalWithNulls() {
        // given
        NonogramFilterRequest a = new NonogramFilterRequest(
                null, List.of("2024"), null,
                null, 3.0,
                null, 7, null, 8
        );
        NonogramFilterRequest b = new NonogramFilterRequest(
                null, List.of("2024"), null,
                null, 3.0,
                null, 7, null, 8
        );

        // when
        int a_hash = a.hashCode();
        int b_hash= b.hashCode();

        // then
        assertThat(a).isEqualTo(b);
        assertThat(a_hash).isEqualTo(b_hash);
    }

    @Test
    @DisplayName("toString should contain class name and field values")
    void toString_shouldContainClassNameAndFields() {
        // given
        NonogramFilterRequest request = new NonogramFilterRequest(
                List.of("src"), List.of("2024"), List.of("07"),
                1.0, 5.0,
                10, 20, 15, 25
        );

        // when
        String result = request.toString();

        // then
        assertThat(result)
                .isNotNull()
                .contains("NonogramFilterRequest(")
                .contains("sources=[src]")
                .contains("years=[2024]")
                .contains("minDifficulty=1.0")
                .contains("maxDifficulty=5.0")
                .contains("minHeight=10")
                .contains("maxHeight=20")
                .contains("minWidth=15")
                .contains("maxWidth=25");
    }

    @Test
    @DisplayName("should correctly set and get values using setters and getters")
    void shouldSetAndGetValuesProperly() {
        // given
        NonogramFilterRequest request = new NonogramFilterRequest();

        List<String> sources = List.of("sys");
        List<String> years = List.of("2025");
        List<String> months = List.of("01");
        Double minDifficulty = 0.5;
        Double maxDifficulty = 9.9;
        Integer minHeight = 3;
        Integer maxHeight = 10;
        Integer minWidth = 4;
        Integer maxWidth = 12;

        // when
        request.setSources(sources);
        request.setYears(years);
        request.setMonths(months);
        request.setMinDifficulty(minDifficulty);
        request.setMaxDifficulty(maxDifficulty);
        request.setMinHeight(minHeight);
        request.setMaxHeight(maxHeight);
        request.setMinWidth(minWidth);
        request.setMaxWidth(maxWidth);

        // then
        assertThat(request.getSources()).isEqualTo(sources);
        assertThat(request.getYears()).isEqualTo(years);
        assertThat(request.getMonths()).isEqualTo(months);
        assertThat(request.getMinDifficulty()).isEqualTo(minDifficulty);
        assertThat(request.getMaxDifficulty()).isEqualTo(maxDifficulty);
        assertThat(request.getMinHeight()).isEqualTo(minHeight);
        assertThat(request.getMaxHeight()).isEqualTo(maxHeight);
        assertThat(request.getMinWidth()).isEqualTo(minWidth);
        assertThat(request.getMaxWidth()).isEqualTo(maxWidth);
    }

}