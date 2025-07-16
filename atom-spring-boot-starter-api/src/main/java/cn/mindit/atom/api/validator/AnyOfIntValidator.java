/*
 * Copyright (c) 2022-2024 Catch(catchlife6@163.com).
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

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Catch
 * @since 2022-10-27
 */
public class AnyOfIntValidator implements ConstraintValidator<AnyOfInt, Integer> {

    private Set<Integer> accepts;

    @Override
    public void initialize(AnyOfInt annotation) {
        accepts = Arrays.stream(annotation.value()).boxed().collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value == null || accepts.contains(value);
    }

}
