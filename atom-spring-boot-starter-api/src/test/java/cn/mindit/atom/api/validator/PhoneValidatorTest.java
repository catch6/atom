/*
 * Copyright (c) 2022-2025 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You may use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.api.validator;

import cn.mindit.atom.api.BaseTest;
import jakarta.validation.ClockProvider;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PhoneValidator 测试类
 *
 * @author Catch
 * @since 2025-01-01
 */
class PhoneValidatorTest extends BaseTest {

    private PhoneValidator phoneValidator;
    private Phone phoneAnnotation;

    @BeforeEach
    void setUp() {
        phoneValidator = new PhoneValidator();
        phoneAnnotation = new Phone() {
            @Override
            public String message() {
                return "手机号格式不正确";
            }

            @Override
            public Class<?>[] groups() {
                return new Class<?>[0];
            }

            @Override
            public Class<? extends jakarta.validation.Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return Phone.class;
            }
        };
        phoneValidator.initialize(phoneAnnotation);
    }

    @Test
    void testNullValue() {
        assertTrue(phoneValidator.isValid(null, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "13812345678",  // 正常手机号
        "15987654321",  // 正常手机号
        "18611112222",  // 正常手机号
        "19933334444"   // 正常手机号
    })
    void testValidPhoneNumbers(String phoneNumber) {
        assertTrue(phoneValidator.isValid(phoneNumber, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",                // 空字符串
        "12345678901",     // 11位但不是1开头
        "1381234567",      // 10位
        "138123456789",    // 12位
        "12812345678",     // 第二位不是3-9
        "1381234567a",     // 包含字母
        "138-1234-5678",   // 包含分隔符
        " 13812345678",    // 包含空格
        "13812345678 ",    // 包含空格
        "abc",             // 纯字母
        "!@#$%^&*()",      // 特殊字符
        "0",               // 单个数字
        "1",               // 单个数字
        "12",              // 两位数字
        "123",             // 三位数字
        "12345678",        // 8位数字
        "123456789012"     // 12位数字
    })
    void testInvalidPhoneNumbers(String phoneNumber) {
        assertFalse(phoneValidator.isValid(phoneNumber, null));
    }

    @Test
    void testInitialize() {
        // 测试初始化方法
        PhoneValidator validator = new PhoneValidator();
        assertDoesNotThrow(() -> validator.initialize(phoneAnnotation));
    }

    @Test
    void testValidatorWithContext() {
        // 创建测试用的验证上下文
        ConstraintValidatorContext context = new ConstraintValidatorContext() {
            @Override
            public void disableDefaultConstraintViolation() {
                // 空实现
            }

            @Override
            public String getDefaultConstraintMessageTemplate() {
                return "手机号格式不正确";
            }

            @Override
            public ClockProvider getClockProvider() {
                return null;
            }

            @Override
            public ConstraintViolationBuilder buildConstraintViolationWithTemplate(String messageTemplate) {
                return null;
            }

            @Override
            public <T> T unwrap(Class<T> type) {
                return null;
            }

        };

        // 测试带上下文的验证
        assertTrue(phoneValidator.isValid("13812345678", context));
        assertFalse(phoneValidator.isValid("123", context));
    }

    @ParameterizedTest
    @CsvSource({
        "13812345678, true",
        "15987654321, true",
        "18611112222, true",
        "19933334444, true",
        "12345678901, false",
        "1381234567, false",
        "138123456789, false",
        "12812345678, false",
        "1381234567a, false",
        "'', false",
        "null, true"
    })
    void testPhoneValidationWithCsv(String phoneNumber, boolean expected) {
        boolean actual = phoneValidator.isValid("null".equals(phoneNumber) ? null : phoneNumber, null);
        assertEquals(expected, actual);
    }

    @Test
    void testEdgeCases() {
        // 测试边界情况
        assertFalse(phoneValidator.isValid("10000000000", null));  // 第二位是0
        assertFalse(phoneValidator.isValid("11000000000", null));  // 第二位是1
        assertFalse(phoneValidator.isValid("12000000000", null));  // 第二位是2
        assertTrue(phoneValidator.isValid("13000000000", null));   // 第二位是3
        assertTrue(phoneValidator.isValid("14000000000", null));   // 第二位是4
        assertTrue(phoneValidator.isValid("15000000000", null));   // 第二位是5
        assertTrue(phoneValidator.isValid("16000000000", null));   // 第二位是6
        assertTrue(phoneValidator.isValid("17000000000", null));   // 第二位是7
        assertTrue(phoneValidator.isValid("18000000000", null));   // 第二位是8
        assertTrue(phoneValidator.isValid("19000000000", null));   // 第二位是9
        assertFalse(phoneValidator.isValid("20000000000", null)); // 第二位是0
    }

    @Test
    void testInternationalPhoneNumbers() {
        // 测试国际手机号格式（应该返回false，因为验证器只支持中国大陆格式）
        assertFalse(phoneValidator.isValid("+8613812345678", null));
        assertFalse(phoneValidator.isValid("008613812345678", null));
        assertFalse(phoneValidator.isValid("8613812345678", null));
    }

    @Test
    void testUnicodeCharacters() {
        // 测试Unicode字符
        assertFalse(phoneValidator.isValid("1381234567８", null));  // 包含全角数字
        assertFalse(phoneValidator.isValid("１３８１２３４５６７８", null)); // 全角数字
        assertFalse(phoneValidator.isValid("138一二三四五六七八", null)); // 中文数字
    }

    @Test
    void testWhitespaceHandling() {
        // 测试空格处理
        assertFalse(phoneValidator.isValid(" 13812345678", null));
        assertFalse(phoneValidator.isValid("13812345678 ", null));
        assertFalse(phoneValidator.isValid(" 13812345678 ", null));
        assertFalse(phoneValidator.isValid("138 1234 5678", null));
        assertFalse(phoneValidator.isValid("138-1234-5678", null));
    }

    @Test
    void testEmptyString() {
        // 测试空字符串
        assertFalse(phoneValidator.isValid("", null));
    }

    @Test
    void testPerformance() {
        // 测试性能 - 验证大量手机号
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            phoneValidator.isValid("13812345678", null);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // 验证应该在合理时间内完成（这里设置100ms作为阈值）
        assertTrue(duration < 100, "手机号验证性能测试失败，耗时: " + duration + "ms");
    }

}