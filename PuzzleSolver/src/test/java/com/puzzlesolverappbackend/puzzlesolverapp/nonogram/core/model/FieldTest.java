package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FieldTest {

    @Test
    @DisplayName("AllArgsConstructor and getters should correctly initialize and return field values")
    void constructorAndGettersShouldWorkCorrectly() {
        // given & when
        Field field = new Field(4, 6);

        // then
        assertThat(field.getRowIdx()).isEqualTo(4);
        assertThat(field.getColumnIdx()).isEqualTo(6);
    }

    @Test
    @DisplayName("Setters should correctly update field values")
    void settersShouldWorkCorrectly() {
        // given
        Field field = new Field(0, 0);

        // when
        field.setRowIdx(5);
        field.setColumnIdx(8);

        // then
        assertThat(field.getRowIdx()).isEqualTo(5);
        assertThat(field.getColumnIdx()).isEqualTo(8);
    }

    @Test
    @DisplayName("equals and hashCode should work for both equal and non-equal fields")
    void equalsAndHashCodeShouldWorkForSameAndDifferentValues() {
        // given && when
        Field f1 = new Field(3, 5);
        Field f2 = new Field(3, 5);
        Field f3 = new Field(4, 5);

        // then

        // Positive case
        assertThat(f1).isEqualTo(f2);
        assertThat(f1).hasSameHashCodeAs(f2);

        // Negative case
        assertThat(f1).isNotEqualTo(f3);
        assertThat(f1.hashCode()).isNotEqualTo(f3.hashCode());
    }

    @Test
    @DisplayName("equals should return true when comparing with itself (this==o)")
    void equalsReturnsTrueForSameReference() {
        // given & when
        Field f = new Field(1, 2);

        // then
        assertThat(f.equals(f)).isTrue();
    }

    @Test
    @DisplayName("equals should return false when other is not a Field (or null)")
    void equalsReturnsFalseForDifferentTypeOrNull() {
        // given & when
        Field f = new Field(1, 2);

        // then
        assertThat(f.equals("not a field")).isFalse();
        assertThat(f.equals(null)).isFalse();
    }

    @Test
    @DisplayName("toString should return the correct format")
    void toStringShouldReturnCorrectFormat() {
        // given & when
        Field field = new Field(2, 7);

        // then
        assertThat(field.toString()).hasToString("Field(rowIdx=2, columnIdx=7)");
    }
}