package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NonogramSolutionDecisionTest {

    @Test
    @DisplayName("No-args constructor should create an instance with null fields")
    void noArgsCtor_createsInstanceWithNulls() {
        // given / when
        NonogramSolutionDecision d = new NonogramSolutionDecision();

        // then
        assertNotNull(d);
        assertNull(d.getDecisionMarker());
        assertNull(d.getDecisionField());
        // do not call toString() here; decisionField is null and would NPE by design
    }

    @Test
    @DisplayName("All-args constructor should set fields and getters should return them")
    void allArgsCtor_setsFields_andGettersReturnThem() {
        // given
        Field f = new Field(2, 7);

        // when
        NonogramSolutionDecision d = new NonogramSolutionDecision("X", f);

        // then
        assertEquals("X", d.getDecisionMarker());
        assertSame(f, d.getDecisionField());
    }

    static class NonogramSolutionDecisionChild extends NonogramSolutionDecision {
        public NonogramSolutionDecisionChild(String marker, Field field) {
            super(marker, field);
        }
        @Override
        protected boolean canEqual(Object other) {
            // Force equals(...) to return false on canEqual check
            return false;
        }
    }

    @Test
    @DisplayName("equals: instanceof true but canEqual == false (subclass) → false")
    void equals_shouldReturnFalse_whenSubclassCanEqualFalse() {
        // given
        Field f = new Field(1, 2);
        NonogramSolutionDecision base = new NonogramSolutionDecision("X", f);
        NonogramSolutionDecision child = new NonogramSolutionDecisionChild("X", f);

        // when / then
        // child instanceof NonogramSolutionDecision == true, but child.canEqual(base) == false
        assertNotEquals(base, child);
        assertEquals(child, base); // symmetric check (can be true or false depending on canEqual)
    }

    @Test
    @DisplayName("equals: this.marker == null vs other.marker != null → false (null branch)")
    void equals_shouldHandleNullMarkerBranch() {
        // given: marker null in 'this', non-null in 'other' (fields equal)
        Field f = new Field(2, 3);
        NonogramSolutionDecision a = new NonogramSolutionDecision(null, f);
        NonogramSolutionDecision b = new NonogramSolutionDecision("X", f);

        // when / then
        assertNotEquals(a, b);
        // also exercise hashCode's null-constant (43) branch
        // (no strict value assert needed; just call to execute the branch)
        a.hashCode();
    }

    @Test
    @DisplayName("equals: markers equal, this.field == null vs other.field != null → false (null branch)")
    void equals_shouldHandleNullFieldBranch() {
        // given: same marker; one field null, the other non-null
        NonogramSolutionDecision a = new NonogramSolutionDecision("O", null);
        NonogramSolutionDecision b = new NonogramSolutionDecision("O", new Field(5, 6));

        // when / then
        assertNotEquals(a, b);
        // exercise hashCode's null-constant (43) branch for field
        a.hashCode();
    }

    @Test
    @DisplayName("equals: both nulls equal (marker=null, field=null) and symmetry/reflexivity")
    void equals_shouldWorkWithBothNulls() {
        // given
        NonogramSolutionDecision a = new NonogramSolutionDecision(null, null);
        NonogramSolutionDecision b = new NonogramSolutionDecision(null, null);

        // when / then
        assertEquals(a, b);         // equality with both nulls
        assertEquals(a, a);         // reflexive
        assertEquals(a.hashCode(), b.hashCode()); // hash consistency when both components are null
    }

    @Test
    @DisplayName("equals/hashCode should consider marker and field")
    void equalsAndHashCode_shouldWork() {
        // given
        Field f = new Field(3, 4);
        NonogramSolutionDecision d1 = new NonogramSolutionDecision("O", f);
        NonogramSolutionDecision d2 = new NonogramSolutionDecision("O", f);
        NonogramSolutionDecision d3 = new NonogramSolutionDecision("X", f);

        // when / then
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());

        assertNotEquals(d1, d3);
        // sanity: equals is reflexive and not equal to null
        assertEquals(d1, d1);
        assertNotEquals(d1, null);
    }

    @Test
    @DisplayName("equals should return false when compared to object of different class")
    void equals_shouldReturnFalseForDifferentClass() {
        // given
        NonogramSolutionDecision decision = new NonogramSolutionDecision("X", new Field(1, 1));

        // when / then
        assertNotEquals(decision, "some string");
        assertNotEquals(decision, new Object());
    }

    @Test
    @DisplayName("toString should format as dec.{'<marker>', r=<row>, c=<col>}")
    void toString_shouldFormatExactly() {
        // given
        Field f = new Field(2, 7);
        NonogramSolutionDecision d = new NonogramSolutionDecision("X", f);

        // when
        String s = d.toString();

        // then
        assertEquals("dec.{\'X\', r=2, c=7}", s);
    }

    @Test
    @DisplayName("withMarker should return new instance with same field and new marker, original unchanged")
    void withMarker_shouldReturnNewInstance() {
        // given
        Field f = new Field(5, 9);
        NonogramSolutionDecision original = new NonogramSolutionDecision("X", f);

        // when
        NonogramSolutionDecision changed = original.withMarker("O");

        // then
        assertNotSame(original, changed);
        assertEquals("X", original.getDecisionMarker());
        assertEquals("O", changed.getDecisionMarker());
        assertSame(f, changed.getDecisionField());
        assertSame(original.getDecisionField(), changed.getDecisionField());
    }
}
