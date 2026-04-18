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

class AnyOfEnumValidatorTest {

    enum Status {
        ACTIVE, INACTIVE, PENDING
    }

    enum OtherEnum {
        ACTIVE
    }

    @AnyOfEnum(Status.class)
    private Status field;

    private AnyOfEnumValidator validator;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        AnyOfEnum annotation = getClass().getDeclaredField("field").getAnnotation(AnyOfEnum.class);
        validator = new AnyOfEnumValidator();
        validator.initialize(annotation);
    }

    @Test
    void nullIsValid() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @Test
    void allEnumConstantsAreValid() {
        for (Status s : Status.values()) {
            assertThat(validator.isValid(s, null)).as("Status.%s", s).isTrue();
        }
    }

    @Test
    void otherEnumWithSameNameIsInvalid() {
        // 不同枚举类型的同名常量不应被接受
        assertThat(validator.isValid(OtherEnum.ACTIVE, null)).isFalse();
    }

}
