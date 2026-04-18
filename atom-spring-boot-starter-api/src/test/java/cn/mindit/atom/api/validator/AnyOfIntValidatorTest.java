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

class AnyOfIntValidatorTest {

    @AnyOfInt({1, 2, 3})
    private Integer field;

    private AnyOfIntValidator validator;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        AnyOfInt annotation = getClass().getDeclaredField("field").getAnnotation(AnyOfInt.class);
        validator = new AnyOfIntValidator();
        validator.initialize(annotation);
    }

    @Test
    void nullIsValid() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @Test
    void valueInAcceptsIsValid() {
        assertThat(validator.isValid(1, null)).isTrue();
        assertThat(validator.isValid(2, null)).isTrue();
        assertThat(validator.isValid(3, null)).isTrue();
    }

    @Test
    void valueNotInAcceptsIsInvalid() {
        assertThat(validator.isValid(0, null)).isFalse();
        assertThat(validator.isValid(4, null)).isFalse();
        assertThat(validator.isValid(-1, null)).isFalse();
        assertThat(validator.isValid(Integer.MAX_VALUE, null)).isFalse();
    }

}
