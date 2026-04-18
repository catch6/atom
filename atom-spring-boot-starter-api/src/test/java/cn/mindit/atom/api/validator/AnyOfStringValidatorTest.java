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

package cn.mindit.atom.api.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnyOfStringValidatorTest {

    @AnyOfString({"ACTIVE", "INACTIVE", "PENDING"})
    private String field;

    private AnyOfStringValidator validator;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        AnyOfString annotation = getClass().getDeclaredField("field").getAnnotation(AnyOfString.class);
        validator = new AnyOfStringValidator();
        validator.initialize(annotation);
    }

    @Test
    void nullIsValid() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @Test
    void valueInAcceptsIsValid() {
        assertThat(validator.isValid("ACTIVE", null)).isTrue();
        assertThat(validator.isValid("INACTIVE", null)).isTrue();
        assertThat(validator.isValid("PENDING", null)).isTrue();
    }

    @Test
    void valueNotInAcceptsIsInvalid() {
        assertThat(validator.isValid("UNKNOWN", null)).isFalse();
        assertThat(validator.isValid("", null)).isFalse();
        assertThat(validator.isValid("active", null)).isFalse(); // 大小写敏感
    }

    @Test
    void charSequenceOtherThanStringIsAccepted() {
        assertThat(validator.isValid(new StringBuilder("ACTIVE"), null)).isTrue();
        assertThat(validator.isValid(new StringBuilder("UNKNOWN"), null)).isFalse();
    }

}
