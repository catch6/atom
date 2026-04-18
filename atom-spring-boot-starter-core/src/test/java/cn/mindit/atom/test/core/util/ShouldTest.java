/*
 * Copyright (c) 2022-2026 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.test.core.util;

import cn.mindit.atom.core.util.BusinessException;
import cn.mindit.atom.core.util.Result;
import cn.mindit.atom.core.util.Should;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShouldTest {

    @Test
    void throwsBusinessExceptionNotServiceException() {
        assertThatThrownBy(() -> Should.isTrue(false, "m"))
            .isInstanceOf(BusinessException.class);
    }

    @Nested
    class EqualsAssertions {
        @Test
        void isEqualsPassesWhenEqual() {
            assertThatCode(() -> Should.isEquals(1, 1, "m")).doesNotThrowAnyException();
        }

        @Test
        void isEqualsThrowsAllOverloads() {
            assertThatThrownBy(() -> Should.isEquals(1, 2, "m")).isInstanceOf(BusinessException.class).hasMessage("m");
            assertThatThrownBy(() -> Should.isEquals(1, 2, 7, "m"))
                .isInstanceOfSatisfying(BusinessException.class, ex -> assertThat(ex.getCode()).isEqualTo(7));
            assertThatThrownBy(() -> Should.isEquals(1, 2, Result.fail(8, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEquals(1, 2, () -> "m")).isInstanceOf(BusinessException.class).hasMessage("m");
            assertThatThrownBy(() -> Should.isEquals(1, 2, 9, () -> "m")).isInstanceOf(BusinessException.class);
        }

        @Test
        void notEqualsThrowsWhenEqual() {
            assertThatThrownBy(() -> Should.notEquals(1, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEquals(1, 1, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEquals(1, 1, Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEquals(1, 1, () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEquals(1, 1, 1, () -> "m")).isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    class BooleanAssertions {
        @Test
        void isTrueThrowsWhenFalse() {
            assertThatThrownBy(() -> Should.isTrue(false, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isTrue(false, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isTrue(false, Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isTrue(false, () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isTrue(false, 1, () -> "m")).isInstanceOf(BusinessException.class);
        }

        @Test
        void isFalseThrowsWhenTrue() {
            assertThatThrownBy(() -> Should.isFalse(true, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isFalse(true, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isFalse(true, Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isFalse(true, () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isFalse(true, 1, () -> "m")).isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    class NullAssertions {
        @Test
        void isNullThrowsWhenNotNull() {
            assertThatThrownBy(() -> Should.isNull("x", "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isNull("x", 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isNull("x", Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isNull("x", () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isNull("x", 1, () -> "m")).isInstanceOf(BusinessException.class);
        }

        @Test
        void notNullThrowsWhenNull() {
            assertThatThrownBy(() -> Should.notNull(null, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notNull(null, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notNull(null, Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notNull(null, () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notNull(null, 1, () -> "m")).isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    class StringAssertions {
        @Test
        void isEmptyPassesForNullOrEmpty() {
            assertThatCode(() -> Should.isEmpty((String) null, "m")).doesNotThrowAnyException();
            assertThatCode(() -> Should.isEmpty("", "m")).doesNotThrowAnyException();
        }

        @Test
        void isEmptyThrowsForNonEmpty() {
            assertThatThrownBy(() -> Should.isEmpty("x", "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty("x", 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty("x", Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty("x", () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty("x", 1, () -> "m")).isInstanceOf(BusinessException.class);
        }

        @Test
        void notEmptyThrowsForEmpty() {
            assertThatThrownBy(() -> Should.notEmpty("", "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty((String) null, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty("", Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty("", () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty("", 1, () -> "m")).isInstanceOf(BusinessException.class);
        }

        @Test
        void isBlankPassesForBlank() {
            assertThatCode(() -> Should.isBlank(null, "m")).doesNotThrowAnyException();
            assertThatCode(() -> Should.isBlank("   ", "m")).doesNotThrowAnyException();
        }

        @Test
        void isBlankThrowsForText() {
            assertThatThrownBy(() -> Should.isBlank("x", "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isBlank("x", 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isBlank("x", Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isBlank("x", () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isBlank("x", 1, () -> "m")).isInstanceOf(BusinessException.class);
        }

        @Test
        void notBlankThrowsForBlank() {
            assertThatThrownBy(() -> Should.notBlank("   ", "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notBlank(null, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notBlank("   ", Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notBlank("   ", () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notBlank("   ", 1, () -> "m")).isInstanceOf(BusinessException.class);
        }

        @Test
        void containsAssertions() {
            assertThatCode(() -> Should.isContains("abc", "a", "m")).doesNotThrowAnyException();
            assertThatThrownBy(() -> Should.isContains("abc", "z", "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isContains("abc", "z", 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isContains("abc", "z", Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isContains("abc", "z", () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isContains("abc", "z", 1, () -> "m")).isInstanceOf(BusinessException.class);

            assertThatCode(() -> Should.notContains("abc", "z", "m")).doesNotThrowAnyException();
            assertThatThrownBy(() -> Should.notContains("abc", "a", "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notContains("abc", "a", 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notContains("abc", "a", Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notContains("abc", "a", () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notContains("abc", "a", 1, () -> "m")).isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    class ArrayCollectionMapAssertions {
        @Test
        void arrayAssertions() {
            Object[] nonEmpty = {1};
            Object[] withNull = {"a", null};
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, 1, () -> "m")).isInstanceOf(BusinessException.class);

            assertThatThrownBy(() -> Should.notEmpty((Object[]) null, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty(new Object[0], 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty(new Object[0], Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty(new Object[0], () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty(new Object[0], 1, () -> "m")).isInstanceOf(BusinessException.class);

            assertThatThrownBy(() -> Should.noNullElements(withNull, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.noNullElements(withNull, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.noNullElements(withNull, Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.noNullElements(withNull, () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.noNullElements(withNull, 1, () -> "m")).isInstanceOf(BusinessException.class);
        }

        @Test
        void collectionAssertions() {
            List<Integer> nonEmpty = List.of(1);
            java.util.List<Object> withNull = new java.util.ArrayList<>();
            withNull.add(null);
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, 1, () -> "m")).isInstanceOf(BusinessException.class);

            assertThatThrownBy(() -> Should.notEmpty(Collections.emptyList(), "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty(Collections.emptyList(), 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty(Collections.emptyList(), Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty(Collections.emptyList(), () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty(Collections.emptyList(), 1, () -> "m")).isInstanceOf(BusinessException.class);

            assertThatThrownBy(() -> Should.noNullElements(withNull, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.noNullElements(withNull, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.noNullElements(withNull, Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.noNullElements(withNull, () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.noNullElements(withNull, 1, () -> "m")).isInstanceOf(BusinessException.class);
        }

        @Test
        void mapAssertions() {
            Map<String, Integer> nonEmpty = Map.of("a", 1);
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isEmpty(nonEmpty, 1, () -> "m")).isInstanceOf(BusinessException.class);

            assertThatThrownBy(() -> Should.notEmpty((Map<?, ?>) null, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty(Map.of(), 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty(Map.of(), Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty(Map.of(), () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notEmpty(Map.of(), 1, () -> "m")).isInstanceOf(BusinessException.class);
        }
    }

    @Nested
    class TypeAssertions {
        @Test
        void isInstanceOfThrowsForMismatch() {
            assertThatThrownBy(() -> Should.isInstanceOf(String.class, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isInstanceOf(String.class, 1, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isInstanceOf(String.class, 1, Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isInstanceOf(String.class, 1, () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isInstanceOf(String.class, 1, 1, () -> "m")).isInstanceOf(BusinessException.class);
        }

        @Test
        void notInstanceOfThrowsForMatch() {
            assertThatThrownBy(() -> Should.notInstanceOf(String.class, "x", "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notInstanceOf(String.class, "x", 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notInstanceOf(String.class, "x", Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notInstanceOf(String.class, "x", () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notInstanceOf(String.class, "x", 1, () -> "m")).isInstanceOf(BusinessException.class);
        }

        @Test
        void isAssignableThrowsForUnrelated() {
            assertThatThrownBy(() -> Should.isAssignable(Number.class, String.class, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isAssignable(Number.class, null, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isAssignable(Number.class, String.class, Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isAssignable(Number.class, String.class, () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.isAssignable(Number.class, String.class, 1, () -> "m")).isInstanceOf(BusinessException.class);
        }

        @Test
        void notAssignableThrowsForSubtype() {
            assertThatThrownBy(() -> Should.notAssignable(Number.class, Integer.class, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notAssignable(Number.class, Integer.class, 1, "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notAssignable(Number.class, Integer.class, Result.fail(1, "m"))).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notAssignable(Number.class, Integer.class, () -> "m")).isInstanceOf(BusinessException.class);
            assertThatThrownBy(() -> Should.notAssignable(Number.class, Integer.class, 1, () -> "m")).isInstanceOf(BusinessException.class);
        }
    }

}
