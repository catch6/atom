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
 * AnyOfStringValidator 测试类
 *
 * @author Catch
 * @since 2025-01-01
 */
class AnyOfStringValidatorTest extends BaseTest {

    private AnyOfStringValidator validator;
    private AnyOfString annotation;

    @BeforeEach
    void setUp() {
        validator = new AnyOfStringValidator();

        // 创建测试用的注解实例
        annotation = new AnyOfString() {
            @Override
            public String[] value() {
                return new String[]{"ACTIVE", "INACTIVE", "PENDING"};
            }

            @Override
            public String message() {
                return "值必须是 [ACTIVE, INACTIVE, PENDING] 中的一个";
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
                return AnyOfString.class;
            }
        };

        validator.initialize(annotation);
    }

    @Test
    void testNullValue() {
        assertTrue(validator.isValid(null, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ACTIVE", "INACTIVE", "PENDING"})
    void testValidValues(String value) {
        assertTrue(validator.isValid(value, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "active",        // 小写
        "Active",        // 首字母大写
        "inactive",      // 小写
        "Inactive",      // 首字母大写
        "pending",       // 小写
        "Pending",       // 首字母大写
        "DELETED",       // 不在列表中的值
        "BLOCKED",       // 不在列表中的值
        "",              // 空字符串
        " ",             // 空格
        "ACTIVE_INACTIVE", // 组合值
        "A",             // 单个字符
        "123",           // 数字
        "!@#$",          // 特殊字符
        "ACTIVE ",        // 带空格
        " ACTIVE",        // 带前导空格
        " ACTIVE "        // 带前后空格
    })
    void testInvalidValues(String value) {
        assertFalse(validator.isValid(value, null));
    }

    @Test
    void testInitializeWithDifferentValues() {
        AnyOfStringValidator newValidator = new AnyOfStringValidator();

        AnyOfString newAnnotation = new AnyOfString() {
            @Override
            public String[] value() {
                return new String[]{"YES", "NO", "MAYBE"};
            }

            @Override
            public String message() {
                return "值必须是 [YES, NO, MAYBE] 中的一个";
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
                return AnyOfString.class;
            }
        };

        newValidator.initialize(newAnnotation);

        assertTrue(newValidator.isValid("YES", null));
        assertTrue(newValidator.isValid("NO", null));
        assertTrue(newValidator.isValid("MAYBE", null));
        assertFalse(newValidator.isValid("ACTIVE", null));
    }

    @Test
    void testInitializeWithEmptyArray() {
        AnyOfStringValidator emptyValidator = new AnyOfStringValidator();

        AnyOfString emptyAnnotation = new AnyOfString() {
            @Override
            public String[] value() {
                return new String[]{};
            }

            @Override
            public String message() {
                return "值必须是 [] 中的一个";
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
                return AnyOfString.class;
            }
        };

        emptyValidator.initialize(emptyAnnotation);

        // 空数组应该只接受null值
        assertTrue(emptyValidator.isValid(null, null));
        assertFalse(emptyValidator.isValid("ANY_VALUE", null));
    }

    @Test
    void testInitializeWithSingleValue() {
        AnyOfStringValidator singleValidator = new AnyOfStringValidator();

        AnyOfString singleAnnotation = new AnyOfString() {
            @Override
            public String[] value() {
                return new String[]{"SINGLE"};
            }

            @Override
            public String message() {
                return "值必须是 [SINGLE] 中的一个";
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
                return AnyOfString.class;
            }
        };

        singleValidator.initialize(singleAnnotation);

        assertTrue(singleValidator.isValid("SINGLE", null));
        assertFalse(singleValidator.isValid("OTHER", null));
    }

    @ParameterizedTest
    @CsvSource({
        "ACTIVE, true",
        "INACTIVE, true",
        "PENDING, true",
        "active, false",
        "DELETED, false",
        "'', false",
        "null, true"
    })
    void testValidationWithCsv(String value, boolean expected) {
        boolean actual = validator.isValid("null".equals(value) ? null : value, null);
        assertEquals(expected, actual);
    }

    @Test
    void testCaseSensitivity() {
        // 测试大小写敏感性
        assertTrue(validator.isValid("ACTIVE", null));
        assertFalse(validator.isValid("active", null));
        assertFalse(validator.isValid("Active", null));
    }

    @Test
    void testDuplicateValuesInAnnotation() {
        AnyOfStringValidator duplicateValidator = new AnyOfStringValidator();

        AnyOfString duplicateAnnotation = new AnyOfString() {
            @Override
            public String[] value() {
                return new String[]{"ACTIVE", "ACTIVE", "INACTIVE"};
            }

            @Override
            public String message() {
                return "值必须是 [ACTIVE, ACTIVE, INACTIVE] 中的一个";
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
                return AnyOfString.class;
            }
        };

        duplicateValidator.initialize(duplicateAnnotation);

        // 即使有重复值，验证器也应该正常工作
        assertTrue(duplicateValidator.isValid("ACTIVE", null));
        assertTrue(duplicateValidator.isValid("INACTIVE", null));
        assertFalse(duplicateValidator.isValid("PENDING", null));
    }

    @Test
    void testSpecialCharacters() {
        AnyOfStringValidator specialValidator = new AnyOfStringValidator();

        AnyOfString specialAnnotation = new AnyOfString() {
            @Override
            public String[] value() {
                return new String[]{"!@#$", "中文", "123", "UPPER", "lower"};
            }

            @Override
            public String message() {
                return "值必须是特殊字符中的一个";
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
                return AnyOfString.class;
            }
        };

        specialValidator.initialize(specialAnnotation);

        assertTrue(specialValidator.isValid("!@#$", null));
        assertTrue(specialValidator.isValid("中文", null));
        assertTrue(specialValidator.isValid("123", null));
        assertTrue(specialValidator.isValid("UPPER", null));
        assertTrue(specialValidator.isValid("lower", null));
        assertFalse(specialValidator.isValid("OTHER", null));
    }

    @Test
    void testPerformance() {
        // 测试性能
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            validator.isValid("ACTIVE", null);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertTrue(duration < 100, "AnyOfStringValidator 性能测试失败，耗时: " + duration + "ms");
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
                return "值必须是 [ACTIVE, INACTIVE, PENDING] 中的一个";
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

        assertTrue(validator.isValid("ACTIVE", context));
        assertFalse(validator.isValid("INVALID", context));
    }

}