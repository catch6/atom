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

class AnyOfLongValidatorTest {

    @AnyOfLong({10L, 20L, 30L})
    private Long field;

    private AnyOfLongValidator validator;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        AnyOfLong annotation = getClass().getDeclaredField("field").getAnnotation(AnyOfLong.class);
        validator = new AnyOfLongValidator();
        validator.initialize(annotation);
    }

    @Test
    void nullIsValid() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @Test
    void valueInAcceptsIsValid() {
        assertThat(validator.isValid(10L, null)).isTrue();
        assertThat(validator.isValid(20L, null)).isTrue();
        assertThat(validator.isValid(30L, null)).isTrue();
    }

    @Test
    void valueNotInAcceptsIsInvalid() {
        assertThat(validator.isValid(0L, null)).isFalse();
        assertThat(validator.isValid(40L, null)).isFalse();
        assertThat(validator.isValid(-1L, null)).isFalse();
        assertThat(validator.isValid(Long.MAX_VALUE, null)).isFalse();
    }

}
