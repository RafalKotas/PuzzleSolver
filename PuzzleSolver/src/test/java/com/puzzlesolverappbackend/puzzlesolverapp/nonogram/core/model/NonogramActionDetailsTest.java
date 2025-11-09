package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


class NonogramActionDetailsTest {

    // --- helpers ---
    private NonogramActionDetails sample(int index) {
        return new NonogramActionDetails(
                index,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,
                false
        );
    }

    // constructor / setters / copy / toString

    @Test
    @DisplayName("all-args constructor should set all fields")
    void allArgsConstructor_setsAllFields() {
        // given
        int idx = 5;
        NonogramSolveAction action = NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW;
        NonogramSolveAction trigger = NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW;

        // when
        NonogramActionDetails d = new NonogramActionDetails(idx, action, trigger, true);

        // then
        assertThat(d.getIndex()).isEqualTo(idx);
        assertThat(d.getActionName()).isEqualTo(action);
        assertThat(d.getTriggeringActionName()).isEqualTo(trigger);
        assertThat(d.isChangedState()).isTrue();
    }

    @Test
    @DisplayName("no-args + setters should populate the instance")
    void noArgsAndSetters_populateFields() {
        // given
        NonogramActionDetails d = new NonogramActionDetails();

        // when
        d.setIndex(7);
        d.setActionName(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN);
        d.setTriggeringActionName(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN);
        d.setChangedState(false);

        // then
        assertThat(d.getIndex()).isEqualTo(7);
        assertThat(d.getActionName()).isEqualTo(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN);
        assertThat(d.getTriggeringActionName()).isEqualTo(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN);
        assertThat(d.isChangedState()).isFalse();
    }

    @Test
    @DisplayName("copy() should clone field values and return a different instance")
    void copy_clonesFields_notSameInstance() {
        // given
        NonogramActionDetails original = new NonogramActionDetails(
                42,
                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,
                true
        );

        // when
        NonogramActionDetails copy = NonogramActionDetails.copy(original);

        // then
        assertThat(copy).isNotSameAs(original);
        assertThat(copy.getIndex()).isEqualTo(original.getIndex());
        assertThat(copy.getActionName()).isEqualTo(original.getActionName());
        assertThat(copy.getTriggeringActionName()).isEqualTo(original.getTriggeringActionName());
        assertThat(copy.isChangedState()).isEqualTo(original.isChangedState());
    }

    @Test
    @DisplayName("toString() should contain index and action names")
    void toString_containsKeyData() {
        // given
        NonogramActionDetails d = new NonogramActionDetails(
                9,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                false
        );

        // when
        String s = d.toString();

        // then
        assertThat(s).contains("NonogramActionDetails")
                .contains("index=9")
                .contains(NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW.name())
                .contains(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW.name())
                .contains("changedState=false");
    }

    // --- equals / hashCode ---

    @Test
    @DisplayName("equals is reflexive for the same instance")
    void equals_reflexive() {
        // given
        NonogramActionDetails a = sample(1);

        // when & then
        assertThat(a.equals(a)).isTrue();
    }

    @Test
    @DisplayName("equals returns false for null and different class")
    void equals_nullAndDifferentClass() {
        // given
        NonogramActionDetails a = sample(1);

        // when & then
        assertThat(a.equals(null)).isFalse();
        assertThat(a.equals("not-an-action")).isFalse();
    }

    @Test
    @DisplayName("equals returns true for two objects with identical fields (symmetry + transitivity)")
    void equals_trueForIdenticalFields() {
        // given
        NonogramActionDetails a = sample(1);
        NonogramActionDetails b = sample(1);
        NonogramActionDetails c = sample(1);

        // when & then
        assertThat(a).isEqualTo(b);
        assertThat(b).isEqualTo(a);
        assertThat(a).isEqualTo(c);
        assertThat(b).isEqualTo(c);
    }

    @Test
    @DisplayName("equals returns false when any field differs")
    void equals_falseWhenAnyFieldDiffers() {
        // given
        NonogramActionDetails base = sample(1);

        NonogramActionDetails diffIndex = new NonogramActionDetails(
                2, base.getActionName(), base.getTriggeringActionName(), base.isChangedState());
        NonogramActionDetails diffAction = new NonogramActionDetails(
                base.getIndex(), NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW,
                base.getTriggeringActionName(), base.isChangedState());
        NonogramActionDetails diffTrigger = new NonogramActionDetails(
                base.getIndex(), base.getActionName(),
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, base.isChangedState());
        NonogramActionDetails diffChanged = new NonogramActionDetails(
                base.getIndex(), base.getActionName(), base.getTriggeringActionName(), true);

        // when & then
        assertThat(base).isNotEqualTo(diffIndex)
                .isNotEqualTo(diffAction)
                .isNotEqualTo(diffTrigger)
                .isNotEqualTo(diffChanged);
    }

    @Test
    @DisplayName("hashCode is consistent with equals (same fields -> same hash)")
    void hashCode_consistentForEqualObjects() {
        // given
        NonogramActionDetails a = sample(3);
        NonogramActionDetails b = sample(3);

        // when // then
        assertThat(a).isEqualTo(b)
                .hasSameHashCodeAs(b);
    }

    @Test
    @DisplayName("hashCode likely differs when significant fields differ")
    void hashCode_differsForDifferentObjects() {
        // given
        NonogramActionDetails a = sample(3);
        NonogramActionDetails b = sample(4);

        // when // then
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());
    }

    // --- ActionDependencyMap linkage smoke ---

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("action should be present in ActionDependencyMap for its triggering action (sample pairs)")
    void dependencyMap_containsExpectedPairs() throws Exception {
        // given
        Class<?> depsClazz = Class.forName(
                "com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.actions.ActionDependencyMap"
        );
        Field f = depsClazz.getDeclaredField("actionDependencies");
        f.setAccessible(true);
        Map<NonogramSolveAction, List<NonogramSolveAction>> deps =
                (Map<NonogramSolveAction, List<NonogramSolveAction>>) f.get(null);

        // when & then
        assertThat(deps.get(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW))
                .contains(
                        NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW,
                        NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW
                );

        assertThat(deps.get(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN))
                .contains(
                        NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                        NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN
                );

        assertThat(deps.get(NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN))
                .contains(
                        NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN,
                        NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
                );
    }

    @Test
    @DisplayName("equals returns true for the same instance (this == o short-circuit)")
    void equals_isReflexive() {
        // given
        NonogramActionDetails a = new NonogramActionDetails(
                1,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,
                false
        );

        // when & then
        assertThat(a.equals(a)).isTrue(); // covers (this == o) return true
    }

    @Test
    @DisplayName("equals returns false when compared to object of different type")
    void equals_returnsFalseForDifferentType() {
        // given
        NonogramActionDetails a = new NonogramActionDetails(
                1,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,
                false
        );

        // when & then
        assertThat(a.equals("not-a-details")).isFalse(); // covers !(o instanceof NonogramActionDetails)
    }

    @Test
    @DisplayName("equals/hashCode handle null actionName and triggeringActionName (both null on both sides)")
    void equalsAndHash_handleNulls_bothSidesNull() {
        // given
        NonogramActionDetails x = new NonogramActionDetails(7, null, null, true);
        NonogramActionDetails y = new NonogramActionDetails(7, null, null, true);

        // when & then
        assertThat(x).isEqualTo(y)
                .hasSameHashCodeAs(y); // cover hashCode branches with nulls
    }

    @Test
    @DisplayName("equals returns false when one actionName is null and the other is non-null")
    void equals_handlesMixedNull_actionName() {
        // given
        NonogramActionDetails x = new NonogramActionDetails(
                10, null, NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, false);
        NonogramActionDetails y = new NonogramActionDetails(
                10, NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, false);

        // when & then
        assertThat(x.equals(y)).isFalse(); // branch: this$actionName == null ? other$actionName != null -> true
    }

    @Test
    @DisplayName("equals returns false when one triggeringActionName is null and the other is non-null")
    void equals_handlesMixedNull_triggeringActionName() {
        // given
        NonogramActionDetails x = new NonogramActionDetails(
                3, NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, null, false);
        NonogramActionDetails y = new NonogramActionDetails(
                3, NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, false);

        // when & then
        assertThat(x.equals(y)).isFalse(); // branch: this$triggeringActionName == null ? other$triggeringActionName != null -> true
    }

    @Test
    @DisplayName("equals hits canEqual guard: base.equals(subclassWithFalseCanEqual) returns false")
    void equals_triggersCanEqualGuard_branch() {
        // given: subclass that deliberately overrides canEqual to always return false
        class DetailsWithFalseCanEqual extends NonogramActionDetails {
            DetailsWithFalseCanEqual(int i, NonogramSolveAction a, NonogramSolveAction t, boolean c) {
                super(i, a, t, c);
            }
            @Override
            protected boolean canEqual(Object other) {
                return false; // force canEqual guard to fail
            }
        }

        // base class instance
        NonogramActionDetails base = new NonogramActionDetails(
                1,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,
                false
        );

        // subclass instance with canEqual always returning false
        NonogramActionDetails sub = new DetailsWithFalseCanEqual(
                1,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,
                false
        );

        // when & then:
        // Calling base.equals(sub) will hit the guard:
        // if (!other.canEqual(this)) return false;
        assertThat(base.equals(sub)).isFalse();

        // Note: calling sub.equals(base) will return true
        // because sub's equals still uses the base implementation,
        // and base.canEqual(sub) returns true.
        // This intentionally breaks symmetry for testing purposes.
        assertThat(sub.equals(base)).isTrue();
    }
}