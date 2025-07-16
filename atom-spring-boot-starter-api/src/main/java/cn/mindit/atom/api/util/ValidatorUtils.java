/*
 * Copyright (c) 2022-2025 Catch(catchlife6@163.com).
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

import jakarta.validation.*;
import org.hibernate.validator.HibernateValidator;

import java.util.Set;

/**
 * Spring Bean 字段验证工具类
 * <p>
 * 可通过 {@code @Autowired} 注入 Spring 管理的 {@code jakarta.validation.Validator}
 *
 * @author Catch
 * @since 2022-02-15
 */
public class ValidatorUtils {

    /**
     * 快速失败,当遇到校验失败立刻中断返回
     */
    public static final Validator VALIDATOR_FAST;
    /**
     * 完整校验,当遇到校验失败不会中断返回,直到校验完所有字段
     */
    public static final Validator VALIDATOR_ALL;

    static {
        try (ValidatorFactory factory = Validation.byProvider(HibernateValidator.class).configure().failFast(true)
                                                  .buildValidatorFactory()) {
            VALIDATOR_FAST = factory.getValidator();
        }
        try (ValidatorFactory factory = Validation.byProvider(HibernateValidator.class).configure().failFast(false)
                                                  .buildValidatorFactory()) {
            VALIDATOR_ALL = factory.getValidator();
        }
    }

    /**
     * 校验对象,可通过 {@code @Autowired} 注入 Spring 管理的 {@code jakarta.validation.Validator}
     *
     * @param validator 验证器实例
     * @param object    需要验证的对象
     * @param groups    验证分组
     * @throws ConstraintViolationException 如果验证失败抛出此异常
     */
    public static void validate(Validator validator, Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    /**
     * 校验对象的指定属性
     *
     * @param validator    验证器实例
     * @param object       需要验证的对象
     * @param propertyName 需要验证的属性名
     * @param groups       验证分组
     * @throws ConstraintViolationException 如果验证失败抛出此异常
     */
    public static void validateProperty(Validator validator, Object object, String propertyName, Class<?>... groups) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validateProperty(object, propertyName, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    /**
     * 校验指定类的属性值
     *
     * @param <T>          泛型类型
     * @param validator    验证器实例
     * @param clazz        需要验证的类
     * @param propertyName 需要验证的属性名
     * @param value        需要验证的属性值
     * @param groups       验证分组
     * @throws ConstraintViolationException 如果验证失败抛出此异常
     */
    public static <T> void validateValue(Validator validator, Class<T> clazz, String propertyName, Object value, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validateValue(clazz, propertyName, value, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

}
