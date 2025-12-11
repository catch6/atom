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

package cn.mindit.atom.core.util.json;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import tools.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.RoundingMode;

/**
 * @author Catch
 * @since 2024-12-18
 */
@JacksonAnnotation
@JsonSerialize(using = BigDecimalSerializer.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface JsonDecimalFormat {

    /**
     * 指定数字格式化的模式，默认为 "0.00"。
     * <p>
     * 常见的模式：
     * <ul>
     *     <li>0.00：始终显示两位小数</li>
     *     <li>0：只显示整数部分</li>
     *     <li>0.##：最多显示两位小数，不显示尾随零</li>
     *     <li>#,##0: 用逗号分组的整数</li>
     *     <li>#,##0.00：用逗号分组，并显示两位小数</li>
     *     <li>###,###.###：用逗号分组，并显示最多三位小数</li>
     *     <li>0%：将数字乘以100并添加百分号</li>
     *     <li>0.0E0：一位小数的科学计数法</li>
     * </ul>
     */
    String value() default "0.00";

    /**
     * 指定数字格式化的舍入模式，默认为 {@link RoundingMode#HALF_UP}。
     */
    RoundingMode roundingMode() default RoundingMode.HALF_UP;

}
