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

package cn.mindit.atom.api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 手机号验证器实现类
 * 实现Jakarta Validation规范的ConstraintValidator接口
 * 用于验证字符串是否符合中国大陆手机号格式
 *
 * @author Catch
 * @since 2021-04-29
 */
public class PhoneValidator implements ConstraintValidator<Phone, CharSequence> {

    private Pattern pattern;

    /**
     * 初始化手机号验证器，编译手机号正则表达式
     * 正则表达式规则：以 1 开头，第二位是3-9，后面跟9位数字
     *
     * @param constraintAnnotation Phone注解实例
     */
    @Override
    public void initialize(Phone constraintAnnotation) {
        pattern = Pattern.compile("^1[3-9]\\d{9}$");
    }

    /**
     * 验证手机号是否有效
     *
     * @param value   待验证的手机号字符串
     * @param context 验证上下文
     * @return 如果值为null或符合手机号格式返回true，否则返回false
     */
    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        return value == null || pattern.matcher(value).matches();
    }

}


