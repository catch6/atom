/*
 * Copyright (c) 2022-2025 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.test.core.utils;

import cn.mindit.atom.core.util.BusinessException;
import cn.mindit.atom.core.util.ResultProvider;
import cn.mindit.atom.core.util.Should;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Should 工具类测试
 *
 * @author Catch
 * @since 2023-08-08
 */
class ShouldTest {

    @Test
    @DisplayName("测试 isTrue 方法")
    void testIsTrue() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.isTrue(true, "应该为true"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isTrue(false, "应该为true");
        });
        assertEquals("应该为true", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isTrue 方法 - 带代码")
    void testIsTrueWithCode() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isTrue(false, 400, "应该为true");
        });
        assertEquals(400, exception.getCode());
        assertEquals("应该为true", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isTrue 方法 - 带 ResultProvider")
    void testIsTrueWithResultProvider() {
        TestResultProvider provider = new TestResultProvider(500, "服务器错误");
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isTrue(false, provider);
        });
        assertEquals(500, exception.getCode());
        assertEquals("服务器错误", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isTrue 方法 - 带 Supplier")
    void testIsTrueWithSupplier() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isTrue(false, () -> "动态错误消息");
        });
        assertEquals("动态错误消息", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isFalse 方法")
    void testIsFalse() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.isFalse(false, "应该为false"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isFalse(true, "应该为false");
        });
        assertEquals("应该为false", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isNull 方法")
    void testIsNull() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.isNull(null, "应该为null"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isNull("非null值", "应该为null");
        });
        assertEquals("应该为null", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notNull 方法")
    void testNotNull() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.notNull("非null值", "不应该为null"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.notNull(null, "不应该为null");
        });
        assertEquals("不应该为null", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isEmpty 方法 - 字符串")
    void testIsEmptyString() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.isEmpty((String) "", "应该为空字符串"));
        assertDoesNotThrow(() -> Should.isEmpty((String) null, "应该为空字符串"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isEmpty((String) "非空字符串", "应该为空字符串");
        });
        assertEquals("应该为空字符串", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notEmpty 方法 - 字符串")
    void testNotEmptyString() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.notEmpty((String) "非空字符串", "不应该为空字符串"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.notEmpty((String) "", "不应该为空字符串");
        });
        assertEquals("不应该为空字符串", exception.getMessage());
        
        exception = assertThrows(BusinessException.class, () -> {
            Should.notEmpty((String) null, "不应该为空字符串");
        });
        assertEquals("不应该为空字符串", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isBlank 方法")
    void testIsBlank() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.isBlank("", "应该为空白"));
        assertDoesNotThrow(() -> Should.isBlank(null, "应该为空白"));
        assertDoesNotThrow(() -> Should.isBlank("   ", "应该为空白"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isBlank("非空白字符串", "应该为空白");
        });
        assertEquals("应该为空白", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notBlank 方法")
    void testNotBlank() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.notBlank("非空白字符串", "不应该为空白"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.notBlank("", "不应该为空白");
        });
        assertEquals("不应该为空白", exception.getMessage());
        
        exception = assertThrows(BusinessException.class, () -> {
            Should.notBlank(null, "不应该为空白");
        });
        assertEquals("不应该为空白", exception.getMessage());
        
        exception = assertThrows(BusinessException.class, () -> {
            Should.notBlank("   ", "不应该为空白");
        });
        assertEquals("不应该为空白", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isEquals 方法")
    void testIsEquals() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.isEquals("相同", "相同", "应该相等"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isEquals("值1", "值2", "应该相等");
        });
        assertEquals("应该相等", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notEquals 方法")
    void testNotEquals() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.notEquals("值1", "值2", "应该不相等"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.notEquals("相同", "相同", "应该不相等");
        });
        assertEquals("应该不相等", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isContains 方法")
    void testIsContains() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.isContains("Hello World", "World", "应该包含"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isContains("Hello World", "Java", "应该包含");
        });
        assertEquals("应该包含", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notContains 方法")
    void testNotContains() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.notContains("Hello World", "Java", "不应该包含"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.notContains("Hello World", "World", "不应该包含");
        });
        assertEquals("不应该包含", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isEmpty 方法 - 数组")
    void testIsEmptyArray() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.isEmpty((Object[]) new Object[0], "应该为空数组"));
        assertDoesNotThrow(() -> Should.isEmpty((Object[]) null, "应该为空数组"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isEmpty((Object[]) new Object[]{1, 2, 3}, "应该为空数组");
        });
        assertEquals("应该为空数组", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notEmpty 方法 - 数组")
    void testNotEmptyArray() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.notEmpty((Object[]) new Object[]{1, 2, 3}, "不应该为空数组"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.notEmpty((Object[]) new Object[0], "不应该为空数组");
        });
        assertEquals("不应该为空数组", exception.getMessage());
        
        exception = assertThrows(BusinessException.class, () -> {
            Should.notEmpty((Object[]) null, "不应该为空数组");
        });
        assertEquals("不应该为空数组", exception.getMessage());
    }

    @Test
    @DisplayName("测试 noNullElements 方法 - 数组")
    void testNoNullElementsArray() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.noNullElements((Object[]) new Object[]{1, 2, 3}, "不应该有null元素"));
        assertDoesNotThrow(() -> Should.noNullElements((Object[]) null, "不应该有null元素"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.noNullElements((Object[]) new Object[]{1, null, 3}, "不应该有null元素");
        });
        assertEquals("不应该有null元素", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isEmpty 方法 - 集合")
    void testIsEmptyCollection() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.isEmpty((Collection<?>) Collections.emptyList(), "应该为空集合"));
        assertDoesNotThrow(() -> Should.isEmpty((Collection<?>) null, "应该为空集合"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isEmpty((Collection<?>) Arrays.asList(1, 2, 3), "应该为空集合");
        });
        assertEquals("应该为空集合", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notEmpty 方法 - 集合")
    void testNotEmptyCollection() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.notEmpty((Collection<?>) Arrays.asList(1, 2, 3), "不应该为空集合"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.notEmpty((Collection<?>) Collections.emptyList(), "不应该为空集合");
        });
        assertEquals("不应该为空集合", exception.getMessage());
        
        exception = assertThrows(BusinessException.class, () -> {
            Should.notEmpty((Collection<?>) null, "不应该为空集合");
        });
        assertEquals("不应该为空集合", exception.getMessage());
    }

    @Test
    @DisplayName("测试 noNullElements 方法 - 集合")
    void testNoNullElementsCollection() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.noNullElements((Collection<?>) Arrays.asList(1, 2, 3), "不应该有null元素"));
        assertDoesNotThrow(() -> Should.noNullElements((Collection<?>) null, "不应该有null元素"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.noNullElements((Collection<?>) Arrays.asList(1, null, 3), "不应该有null元素");
        });
        assertEquals("不应该有null元素", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isEmpty 方法 - Map")
    void testIsEmptyMap() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.isEmpty((Map<?, ?>) Collections.emptyMap(), "应该为空Map"));
        assertDoesNotThrow(() -> Should.isEmpty((Map<?, ?>) null, "应该为空Map"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isEmpty((Map<?, ?>) Map.of("key", "value"), "应该为空Map");
        });
        assertEquals("应该为空Map", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notEmpty 方法 - Map")
    void testNotEmptyMap() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.notEmpty((Map<?, ?>) Map.of("key", "value"), "不应该为空Map"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.notEmpty((Map<?, ?>) Collections.emptyMap(), "不应该为空Map");
        });
        assertEquals("不应该为空Map", exception.getMessage());
        
        exception = assertThrows(BusinessException.class, () -> {
            Should.notEmpty((Map<?, ?>) null, "不应该为空Map");
        });
        assertEquals("不应该为空Map", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isInstanceOf 方法")
    void testIsInstanceOf() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.isInstanceOf(String.class, "Hello", "应该是String类型"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isInstanceOf(Integer.class, "Hello", "应该是Integer类型");
        });
        assertEquals("应该是Integer类型", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notInstanceOf 方法")
    void testNotInstanceOf() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.notInstanceOf(Integer.class, "Hello", "不应该为Integer类型"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.notInstanceOf(String.class, "Hello", "不应该为String类型");
        });
        assertEquals("不应该为String类型", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isAssignable 方法")
    void testIsAssignable() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.isAssignable(Number.class, Integer.class, "应该可赋值"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.isAssignable(String.class, Integer.class, "应该可赋值");
        });
        assertEquals("应该可赋值", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notAssignable 方法")
    void testNotAssignable() {
        // 测试正常情况
        assertDoesNotThrow(() -> Should.notAssignable(String.class, Integer.class, "不应该可赋值"));
        
        // 测试异常情况
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            Should.notAssignable(Number.class, Integer.class, "不应该可赋值");
        });
        assertEquals("不应该可赋值", exception.getMessage());
    }

    // 测试用的 ResultProvider 实现
    static class TestResultProvider implements ResultProvider {
        private final int code;
        private final String message;
        
        public TestResultProvider(int code, String message) {
            this.code = code;
            this.message = message;
        }
        
        @Override
        public int getCode() {
            return code;
        }
        
        @Override
        public String getMessage() {
            return message;
        }
    }
}