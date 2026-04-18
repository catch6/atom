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

package cn.mindit.atom.api.util;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ValidatorUtilsTest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Sample {

        @NotBlank
        private String name;

        @Min(1)
        private int age;

    }

    private final Validator validator = ValidatorUtils.VALIDATOR_FAST;

    @Test
    void staticValidatorsAreInitialized() {
        assertThat(ValidatorUtils.VALIDATOR_FAST).isNotNull();
        assertThat(ValidatorUtils.VALIDATOR_ALL).isNotNull();
    }

    @Test
    void validatePassesWhenObjectIsValid() {
        Sample sample = new Sample("Alice", 18);
        assertThatCode(() -> ValidatorUtils.validate(validator, sample)).doesNotThrowAnyException();
    }

    @Test
    void validateThrowsWhenObjectIsInvalid() {
        Sample sample = new Sample("", 0);
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> ValidatorUtils.validate(validator, sample))
                .satisfies(e -> assertThat(e.getConstraintViolations()).isNotEmpty());
    }

    @Test
    void validatePropertyPassesWhenPropertyIsValid() {
        Sample sample = new Sample("Alice", 0); // age 非法但不校验这个属性
        assertThatCode(() -> ValidatorUtils.validateProperty(validator, sample, "name"))
                .doesNotThrowAnyException();
    }

    @Test
    void validatePropertyThrowsWhenPropertyIsInvalid() {
        Sample sample = new Sample("", 18);
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> ValidatorUtils.validateProperty(validator, sample, "name"));
    }

    @Test
    void validateValuePassesWhenValueIsValid() {
        assertThatCode(() -> ValidatorUtils.validateValue(validator, Sample.class, "age", 1))
                .doesNotThrowAnyException();
    }

    @Test
    void validateValueThrowsWhenValueIsInvalid() {
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> ValidatorUtils.validateValue(validator, Sample.class, "age", 0));
    }

    @Test
    void fastValidatorStopsAtFirstFailure() {
        Sample sample = new Sample("", 0);
        try {
            ValidatorUtils.validate(ValidatorUtils.VALIDATOR_FAST, sample);
        } catch (ConstraintViolationException e) {
            assertThat(e.getConstraintViolations()).hasSize(1);
        }
    }

    @Test
    void allValidatorCollectsAllFailures() {
        Sample sample = new Sample("", 0);
        try {
            ValidatorUtils.validate(ValidatorUtils.VALIDATOR_ALL, sample);
        } catch (ConstraintViolationException e) {
            assertThat(e.getConstraintViolations()).hasSize(2);
        }
    }

}
