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

package net.wenzuo.atom.api.util;

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
	 */
	public static void validate(Validator validator, Object object, Class<?>... groups) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
		if (!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}

	public static void validateProperty(Validator validator, Object object, String propertyName, Class<?>... groups) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validateProperty(object, propertyName, groups);
		if (!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}

	public static <T> void validateValue(Validator validator, Class<T> clazz, String propertyName, Object value, Class<?>... groups) {
		Set<ConstraintViolation<T>> constraintViolations = validator.validateValue(clazz, propertyName, value, groups);
		if (!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}

}
