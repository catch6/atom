package cn.mindit.atom.test.core.util;

import cn.mindit.atom.core.util.Must;
import cn.mindit.atom.core.util.Result;
import cn.mindit.atom.core.util.ServiceException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MustTest {

    @Nested
    class EqualsAssertions {
        @Test
        void isEqualsPassesWhenEqual() {
            assertThatCode(() -> Must.isEquals("a", "a", "msg")).doesNotThrowAnyException();
        }

        @Test
        void isEqualsThrowsWithMessage() {
            assertThatThrownBy(() -> Must.isEquals("a", "b", "msg"))
                .isInstanceOf(ServiceException.class)
                .hasMessage("msg");
        }

        @Test
        void isEqualsThrowsWithCodeAndMessage() {
            assertThatThrownBy(() -> Must.isEquals("a", "b", 1001, "msg"))
                .isInstanceOfSatisfying(ServiceException.class, ex -> {
                    org.assertj.core.api.Assertions.assertThat(ex.getCode()).isEqualTo(1001);
                    org.assertj.core.api.Assertions.assertThat(ex.getMessage()).isEqualTo("msg");
                });
        }

        @Test
        void isEqualsThrowsWithProvider() {
            assertThatThrownBy(() -> Must.isEquals("a", "b", Result.fail(1002, "fail")))
                .isInstanceOfSatisfying(ServiceException.class, ex -> {
                    org.assertj.core.api.Assertions.assertThat(ex.getCode()).isEqualTo(1002);
                });
        }

        @Test
        void isEqualsThrowsWithSupplier() {
            assertThatThrownBy(() -> Must.isEquals("a", "b", () -> "lazy"))
                .hasMessage("lazy");
            assertThatThrownBy(() -> Must.isEquals("a", "b", 1003, () -> "lazy"))
                .hasMessage("lazy");
        }

        @Test
        void notEqualsPassesWhenDifferent() {
            assertThatCode(() -> Must.notEquals("a", "b", "msg")).doesNotThrowAnyException();
        }

        @Test
        void notEqualsThrowsWhenEqual() {
            assertThatThrownBy(() -> Must.notEquals("a", "a", "msg"))
                .isInstanceOf(ServiceException.class)
                .hasMessage("msg");
            assertThatThrownBy(() -> Must.notEquals("a", "a", 2, "m"))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEquals("a", "a", Result.fail(3, "m")))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEquals("a", "a", () -> "m"))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEquals("a", "a", 4, () -> "m"))
                .isInstanceOf(ServiceException.class);
        }
    }

    @Nested
    class BooleanAssertions {
        @Test
        void isTruePassesWhenTrue() {
            assertThatCode(() -> Must.isTrue(true, "m")).doesNotThrowAnyException();
        }

        @Test
        void isTrueThrowsWhenFalse() {
            assertThatThrownBy(() -> Must.isTrue(false, "m"))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isTrue(false, 1, "m"))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isTrue(false, Result.fail(1, "m")))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isTrue(false, () -> "m"))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isTrue(false, 1, () -> "m"))
                .isInstanceOf(ServiceException.class);
        }

        @Test
        void isFalsePassesWhenFalse() {
            assertThatCode(() -> Must.isFalse(false, "m")).doesNotThrowAnyException();
        }

        @Test
        void isFalseThrowsWhenTrue() {
            assertThatThrownBy(() -> Must.isFalse(true, "m"))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isFalse(true, 1, "m"))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isFalse(true, Result.fail(1, "m")))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isFalse(true, () -> "m"))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isFalse(true, 1, () -> "m"))
                .isInstanceOf(ServiceException.class);
        }
    }

    @Nested
    class NullAssertions {
        @Test
        void isNullPassesWhenNull() {
            assertThatCode(() -> Must.isNull(null, "m")).doesNotThrowAnyException();
        }

        @Test
        void isNullThrowsWhenNotNull() {
            assertThatThrownBy(() -> Must.isNull("x", "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isNull("x", 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isNull("x", Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isNull("x", () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isNull("x", 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void notNullPassesWhenNotNull() {
            assertThatCode(() -> Must.notNull("x", "m")).doesNotThrowAnyException();
        }

        @Test
        void notNullThrowsWhenNull() {
            assertThatThrownBy(() -> Must.notNull(null, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notNull(null, 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notNull(null, Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notNull(null, () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notNull(null, 1, () -> "m")).isInstanceOf(ServiceException.class);
        }
    }

    @Nested
    class StringAssertions {
        @Test
        void isEmptyPassesForNullAndEmpty() {
            assertThatCode(() -> Must.isEmpty((String) null, "m")).doesNotThrowAnyException();
            assertThatCode(() -> Must.isEmpty("", "m")).doesNotThrowAnyException();
        }

        @Test
        void isEmptyThrowsForNonEmpty() {
            assertThatThrownBy(() -> Must.isEmpty("x", "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty("x", 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty("x", Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty("x", () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty("x", 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void notEmptyThrowsForNullAndEmpty() {
            assertThatThrownBy(() -> Must.notEmpty((String) null, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty("", "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty("", 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty("", Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty("", () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty("", 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void notEmptyPassesForNonEmpty() {
            assertThatCode(() -> Must.notEmpty("x", "m")).doesNotThrowAnyException();
        }

        @Test
        void isBlankPassesForNullAndWhitespace() {
            assertThatCode(() -> Must.isBlank(null, "m")).doesNotThrowAnyException();
            assertThatCode(() -> Must.isBlank("   ", "m")).doesNotThrowAnyException();
        }

        @Test
        void isBlankThrowsWhenHasText() {
            assertThatThrownBy(() -> Must.isBlank("x", "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isBlank("x", 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isBlank("x", Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isBlank("x", () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isBlank("x", 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void notBlankThrowsForBlank() {
            assertThatThrownBy(() -> Must.notBlank("   ", "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notBlank("   ", 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notBlank("   ", Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notBlank("   ", () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notBlank("   ", 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void notBlankPassesWhenHasText() {
            assertThatCode(() -> Must.notBlank("x", "m")).doesNotThrowAnyException();
        }

        @Test
        void containsAssertions() {
            // isContains skips null/empty strings
            assertThatCode(() -> Must.isContains(null, "a", "m")).doesNotThrowAnyException();
            assertThatCode(() -> Must.isContains("abc", "a", "m")).doesNotThrowAnyException();
            assertThatThrownBy(() -> Must.isContains("abc", "z", "m"))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isContains("abc", "z", 1, "m"))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isContains("abc", "z", Result.fail(1, "m")))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isContains("abc", "z", () -> "m"))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isContains("abc", "z", 1, () -> "m"))
                .isInstanceOf(ServiceException.class);

            assertThatCode(() -> Must.notContains("abc", "z", "m")).doesNotThrowAnyException();
            assertThatThrownBy(() -> Must.notContains("abc", "a", "m"))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notContains("abc", "a", 1, "m"))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notContains("abc", "a", Result.fail(1, "m")))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notContains("abc", "a", () -> "m"))
                .isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notContains("abc", "a", 1, () -> "m"))
                .isInstanceOf(ServiceException.class);
        }
    }

    @Nested
    class ArrayAssertions {
        @Test
        void isEmptyPassesForNullOrEmpty() {
            assertThatCode(() -> Must.isEmpty((Object[]) null, "m")).doesNotThrowAnyException();
            assertThatCode(() -> Must.isEmpty(new Object[0], "m")).doesNotThrowAnyException();
        }

        @Test
        void isEmptyThrowsForNonEmpty() {
            Object[] arr = {1};
            assertThatThrownBy(() -> Must.isEmpty(arr, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty(arr, 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty(arr, Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty(arr, () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty(arr, 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void notEmptyThrowsForNullOrEmpty() {
            assertThatThrownBy(() -> Must.notEmpty((Object[]) null, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(new Object[0], "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(new Object[0], 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(new Object[0], Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(new Object[0], () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(new Object[0], 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void noNullElementsThrowsWhenContainsNull() {
            Object[] arr = {"a", null};
            assertThatThrownBy(() -> Must.noNullElements(arr, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.noNullElements(arr, 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.noNullElements(arr, Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.noNullElements(arr, () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.noNullElements(arr, 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void noNullElementsPassesWhenAllNonNullOrArrayIsNull() {
            assertThatCode(() -> Must.noNullElements((Object[]) null, "m")).doesNotThrowAnyException();
            assertThatCode(() -> Must.noNullElements(new Object[]{"a"}, "m")).doesNotThrowAnyException();
        }
    }

    @Nested
    class CollectionAssertions {
        @Test
        void isEmptyPassesForNullOrEmpty() {
            assertThatCode(() -> Must.isEmpty((java.util.Collection<?>) null, "m")).doesNotThrowAnyException();
            assertThatCode(() -> Must.isEmpty(Collections.emptyList(), "m")).doesNotThrowAnyException();
        }

        @Test
        void isEmptyThrowsForNonEmpty() {
            List<Integer> coll = List.of(1);
            assertThatThrownBy(() -> Must.isEmpty(coll, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty(coll, 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty(coll, Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty(coll, () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty(coll, 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void notEmptyThrowsForNullOrEmpty() {
            assertThatThrownBy(() -> Must.notEmpty((java.util.Collection<?>) null, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(Collections.emptyList(), "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(Collections.emptyList(), 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(Collections.emptyList(), Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(Collections.emptyList(), () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(Collections.emptyList(), 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void noNullElementsThrowsWhenContainsNull() {
            java.util.List<Object> list = new java.util.ArrayList<>();
            list.add("a");
            list.add(null);
            assertThatThrownBy(() -> Must.noNullElements(list, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.noNullElements(list, 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.noNullElements(list, Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.noNullElements(list, () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.noNullElements(list, 1, () -> "m")).isInstanceOf(ServiceException.class);
        }
    }

    @Nested
    class MapAssertions {
        @Test
        void isEmptyPassesForNullOrEmpty() {
            assertThatCode(() -> Must.isEmpty((Map<?, ?>) null, "m")).doesNotThrowAnyException();
            assertThatCode(() -> Must.isEmpty(Map.of(), "m")).doesNotThrowAnyException();
        }

        @Test
        void isEmptyThrowsForNonEmpty() {
            Map<String, Integer> map = Map.of("a", 1);
            assertThatThrownBy(() -> Must.isEmpty(map, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty(map, 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty(map, Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty(map, () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isEmpty(map, 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void notEmptyThrowsForNullOrEmpty() {
            assertThatThrownBy(() -> Must.notEmpty((Map<?, ?>) null, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(Map.of(), "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(Map.of(), 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(Map.of(), Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(Map.of(), () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notEmpty(Map.of(), 1, () -> "m")).isInstanceOf(ServiceException.class);
        }
    }

    @Nested
    class TypeAssertions {
        @Test
        void isInstanceOfPassesForMatchingType() {
            assertThatCode(() -> Must.isInstanceOf(String.class, "x", "m")).doesNotThrowAnyException();
        }

        @Test
        void isInstanceOfThrowsForMismatch() {
            assertThatThrownBy(() -> Must.isInstanceOf(String.class, 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isInstanceOf(String.class, 1, 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isInstanceOf(String.class, 1, Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isInstanceOf(String.class, 1, () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isInstanceOf(String.class, 1, 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void notInstanceOfThrowsForMatchingType() {
            assertThatThrownBy(() -> Must.notInstanceOf(String.class, "x", "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notInstanceOf(String.class, "x", 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notInstanceOf(String.class, "x", Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notInstanceOf(String.class, "x", () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notInstanceOf(String.class, "x", 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void isAssignablePassesForSubtype() {
            assertThatCode(() -> Must.isAssignable(Number.class, Integer.class, "m")).doesNotThrowAnyException();
        }

        @Test
        void isAssignableThrowsForUnrelatedType() {
            assertThatThrownBy(() -> Must.isAssignable(Number.class, String.class, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isAssignable(Number.class, null, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isAssignable(Number.class, String.class, 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isAssignable(Number.class, String.class, Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isAssignable(Number.class, String.class, () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.isAssignable(Number.class, String.class, 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void notAssignableThrowsForSubtype() {
            assertThatThrownBy(() -> Must.notAssignable(Number.class, Integer.class, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notAssignable(Number.class, Integer.class, 1, "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notAssignable(Number.class, Integer.class, Result.fail(1, "m"))).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notAssignable(Number.class, Integer.class, () -> "m")).isInstanceOf(ServiceException.class);
            assertThatThrownBy(() -> Must.notAssignable(Number.class, Integer.class, 1, () -> "m")).isInstanceOf(ServiceException.class);
        }

        @Test
        void notAssignablePassesWhenSubTypeIsNullOrUnrelated() {
            assertThatCode(() -> Must.notAssignable(Number.class, null, "m")).doesNotThrowAnyException();
            assertThatCode(() -> Must.notAssignable(Number.class, String.class, "m")).doesNotThrowAnyException();
        }
    }

    @Test
    void supplierNullSafeAcceptsNullSupplier() {
        assertThatThrownBy(() -> Must.isTrue(false, (java.util.function.Supplier<String>) null))
            .isInstanceOf(ServiceException.class)
            .hasMessage(null);
    }

}
