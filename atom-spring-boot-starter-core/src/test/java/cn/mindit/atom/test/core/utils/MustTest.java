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

import cn.mindit.atom.core.util.Must;
import cn.mindit.atom.core.util.ResultProvider;
import cn.mindit.atom.core.util.ServiceException;
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
 * Must 工具类测试
 *
 * @author Catch
 * @since 2023-08-08
 */
class MustTest {

    @Test
    @DisplayName("测试 isEquals 方法")
    void testIsEquals() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.isEquals("相同", "相同", "应该相等"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isEquals("值1", "值2", "应该相等");
        });
        assertEquals("应该相等", exception.getMessage());
        assertEquals(ServiceException.DEFAULT_CODE, exception.getCode());
    }

    @Test
    @DisplayName("测试 isEquals 方法 - 带代码")
    void testIsEqualsWithCode() {
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isEquals("值1", "值2", 500, "服务器错误");
        });
        assertEquals(500, exception.getCode());
        assertEquals("服务器错误", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isEquals 方法 - 带 ResultProvider")
    void testIsEqualsWithResultProvider() {
        TestResultProvider provider = new TestResultProvider(404, "资源未找到");
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isEquals("值1", "值2", provider);
        });
        assertEquals(404, exception.getCode());
        assertEquals("资源未找到", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isEquals 方法 - 带 Supplier")
    void testIsEqualsWithSupplier() {
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isEquals("值1", "值2", () -> "动态错误消息");
        });
        assertEquals("动态错误消息", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notEquals 方法")
    void testNotEquals() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.notEquals("值1", "值2", "应该不相等"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.notEquals("相同", "相同", "应该不相等");
        });
        assertEquals("应该不相等", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isTrue 方法")
    void testIsTrue() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.isTrue(true, "应该为true"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isTrue(false, "应该为true");
        });
        assertEquals("应该为true", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isFalse 方法")
    void testIsFalse() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.isFalse(false, "应该为false"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isFalse(true, "应该为false");
        });
        assertEquals("应该为false", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isNull 方法")
    void testIsNull() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.isNull(null, "应该为null"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isNull("非null值", "应该为null");
        });
        assertEquals("应该为null", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notNull 方法")
    void testNotNull() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.notNull("非null值", "不应该为null"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.notNull(null, "不应该为null");
        });
        assertEquals("不应该为null", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isEmpty 方法 - 字符串")
    void testIsEmptyString() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.isEmpty((String) "", "应该为空字符串"));
        assertDoesNotThrow(() -> Must.isEmpty((String) null, "应该为空字符串"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isEmpty((String) "非空字符串", "应该为空字符串");
        });
        assertEquals("应该为空字符串", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notEmpty 方法 - 字符串")
    void testNotEmptyString() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.notEmpty((String) "非空字符串", "不应该为空字符串"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.notEmpty((String) "", "不应该为空字符串");
        });
        assertEquals("不应该为空字符串", exception.getMessage());
        
        exception = assertThrows(ServiceException.class, () -> {
            Must.notEmpty((String) null, "不应该为空字符串");
        });
        assertEquals("不应该为空字符串", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isBlank 方法")
    void testIsBlank() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.isBlank("", "应该为空白"));
        assertDoesNotThrow(() -> Must.isBlank(null, "应该为空白"));
        assertDoesNotThrow(() -> Must.isBlank("   ", "应该为空白"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isBlank("非空白字符串", "应该为空白");
        });
        assertEquals("应该为空白", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notBlank 方法")
    void testNotBlank() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.notBlank("非空白字符串", "不应该为空白"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.notBlank("", "不应该为空白");
        });
        assertEquals("不应该为空白", exception.getMessage());
        
        exception = assertThrows(ServiceException.class, () -> {
            Must.notBlank(null, "不应该为空白");
        });
        assertEquals("不应该为空白", exception.getMessage());
        
        exception = assertThrows(ServiceException.class, () -> {
            Must.notBlank("   ", "不应该为空白");
        });
        assertEquals("不应该为空白", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isContains 方法")
    void testIsContains() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.isContains("Hello World", "World", "应该包含"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isContains("Hello World", "Java", "应该包含");
        });
        assertEquals("应该包含", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notContains 方法")
    void testNotContains() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.notContains("Hello World", "Java", "不应该包含"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.notContains("Hello World", "World", "不应该包含");
        });
        assertEquals("不应该包含", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isEmpty 方法 - 数组")
    void testIsEmptyArray() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.isEmpty((Object[]) new Object[0], "应该为空数组"));
        assertDoesNotThrow(() -> Must.isEmpty((Object[]) null, "应该为空数组"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isEmpty((Object[]) new Object[]{1, 2, 3}, "应该为空数组");
        });
        assertEquals("应该为空数组", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notEmpty 方法 - 数组")
    void testNotEmptyArray() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.notEmpty((Object[]) new Object[]{1, 2, 3}, "不应该为空数组"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.notEmpty((Object[]) new Object[0], "不应该为空数组");
        });
        assertEquals("不应该为空数组", exception.getMessage());
        
        exception = assertThrows(ServiceException.class, () -> {
            Must.notEmpty((Object[]) null, "不应该为空数组");
        });
        assertEquals("不应该为空数组", exception.getMessage());
    }

    @Test
    @DisplayName("测试 noNullElements 方法 - 数组")
    void testNoNullElementsArray() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.noNullElements((Object[]) new Object[]{1, 2, 3}, "不应该有null元素"));
        assertDoesNotThrow(() -> Must.noNullElements((Object[]) null, "不应该有null元素"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.noNullElements((Object[]) new Object[]{1, null, 3}, "不应该有null元素");
        });
        assertEquals("不应该有null元素", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isEmpty 方法 - 集合")
    void testIsEmptyCollection() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.isEmpty((Collection<?>) Collections.emptyList(), "应该为空集合"));
        assertDoesNotThrow(() -> Must.isEmpty((Collection<?>) null, "应该为空集合"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isEmpty((Collection<?>) Arrays.asList(1, 2, 3), "应该为空集合");
        });
        assertEquals("应该为空集合", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notEmpty 方法 - 集合")
    void testNotEmptyCollection() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.notEmpty((Collection<?>) Arrays.asList(1, 2, 3), "不应该为空集合"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.notEmpty((Collection<?>) Collections.emptyList(), "不应该为空集合");
        });
        assertEquals("不应该为空集合", exception.getMessage());
        
        exception = assertThrows(ServiceException.class, () -> {
            Must.notEmpty((Collection<?>) null, "不应该为空集合");
        });
        assertEquals("不应该为空集合", exception.getMessage());
    }

    @Test
    @DisplayName("测试 noNullElements 方法 - 集合")
    void testNoNullElementsCollection() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.noNullElements((Collection<?>) Arrays.asList(1, 2, 3), "不应该有null元素"));
        assertDoesNotThrow(() -> Must.noNullElements((Collection<?>) null, "不应该有null元素"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.noNullElements((Collection<?>) Arrays.asList(1, null, 3), "不应该有null元素");
        });
        assertEquals("不应该有null元素", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isEmpty 方法 - Map")
    void testIsEmptyMap() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.isEmpty((Map<?, ?>) Collections.emptyMap(), "应该为空Map"));
        assertDoesNotThrow(() -> Must.isEmpty((Map<?, ?>) null, "应该为空Map"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isEmpty((Map<?, ?>) Map.of("key", "value"), "应该为空Map");
        });
        assertEquals("应该为空Map", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notEmpty 方法 - Map")
    void testNotEmptyMap() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.notEmpty((Map<?, ?>) Map.of("key", "value"), "不应该为空Map"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.notEmpty((Map<?, ?>) Collections.emptyMap(), "不应该为空Map");
        });
        assertEquals("不应该为空Map", exception.getMessage());
        
        exception = assertThrows(ServiceException.class, () -> {
            Must.notEmpty((Map<?, ?>) null, "不应该为空Map");
        });
        assertEquals("不应该为空Map", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isInstanceOf 方法")
    void testIsInstanceOf() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.isInstanceOf(String.class, "Hello", "应该是String类型"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isInstanceOf(Integer.class, "Hello", "应该是Integer类型");
        });
        assertEquals("应该是Integer类型", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notInstanceOf 方法")
    void testNotInstanceOf() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.notInstanceOf(Integer.class, "Hello", "不应该为Integer类型"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.notInstanceOf(String.class, "Hello", "不应该为String类型");
        });
        assertEquals("不应该为String类型", exception.getMessage());
    }

    @Test
    @DisplayName("测试 isAssignable 方法")
    void testIsAssignable() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.isAssignable(Number.class, Integer.class, "应该可赋值"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isAssignable(String.class, Integer.class, "应该可赋值");
        });
        assertEquals("应该可赋值", exception.getMessage());
    }

    @Test
    @DisplayName("测试 notAssignable 方法")
    void testNotAssignable() {
        // 测试正常情况
        assertDoesNotThrow(() -> Must.notAssignable(String.class, Integer.class, "不应该可赋值"));
        
        // 测试异常情况
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.notAssignable(Number.class, Integer.class, "不应该可赋值");
        });
        assertEquals("不应该可赋值", exception.getMessage());
    }

    @Test
    @DisplayName("测试边界值 - null 参数")
    void testNullParameters() {
        // 测试各种方法的null参数处理
        assertDoesNotThrow(() -> Must.isEquals(null, null, "null应该相等"));
        assertDoesNotThrow(() -> Must.isNull(null, "null应该为null"));
        assertDoesNotThrow(() -> Must.isEmpty((String) null, "null字符串应该为空"));
        assertDoesNotThrow(() -> Must.isEmpty((Object[]) null, "null数组应该为空"));
        assertDoesNotThrow(() -> Must.isEmpty((List<?>) null, "null集合应该为空"));
        assertDoesNotThrow(() -> Must.isEmpty((Map<?, ?>) null, "null Map应该为空"));
        assertThrows(ServiceException.class, () -> Must.isAssignable(String.class, null, "null类型不应该可赋值"));
    }

    @Test
    @DisplayName("测试边界值 - 空值和空集合")
    void testEmptyCollections() {
        // 测试各种空集合的处理
        assertDoesNotThrow(() -> Must.isEmpty(Collections.emptyList(), "空列表应该为空"));
        assertDoesNotThrow(() -> Must.isEmpty(Collections.emptySet(), "空集合应该为空"));
        assertDoesNotThrow(() -> Must.isEmpty(Collections.emptyMap(), "空Map应该为空"));
        assertDoesNotThrow(() -> Must.noNullElements(Collections.emptyList(), "空列表没有null元素"));
        assertDoesNotThrow(() -> Must.noNullElements(Collections.emptySet(), "空集合没有null元素"));
    }

    @Test
    @DisplayName("测试 Supplier 延迟消息")
    void testSupplierMessage() {
        // 测试Supplier的延迟消息生成
        StringBuilder messageBuilder = new StringBuilder();
        Supplier<String> supplier = () -> {
            messageBuilder.append("消息已生成");
            return "动态消息";
        };
        
        // 不应该触发Supplier
        assertDoesNotThrow(() -> Must.isTrue(true, supplier));
        assertEquals("", messageBuilder.toString());
        
        // 应该触发Supplier
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isTrue(false, supplier);
        });
        assertEquals("动态消息", exception.getMessage());
        assertEquals("消息已生成", messageBuilder.toString());
    }

    @Test
    @DisplayName("测试复杂对象比较")
    void testComplexObjectComparison() {
        // 测试复杂对象的相等性比较
        Object obj1 = new Object();
        Object obj2 = new Object();
        
        // 相同对象应该相等
        assertDoesNotThrow(() -> Must.isEquals(obj1, obj1, "相同对象应该相等"));
        
        // 不同对象不应该相等
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            Must.isEquals(obj1, obj2, "不同对象不应该相等");
        });
        assertEquals("不同对象不应该相等", exception.getMessage());
    }

    @Test
    @DisplayName("测试类型检查的边界情况")
    void testTypeCheckingEdgeCases() {
        // 测试null对象的类型检查
        assertThrows(ServiceException.class, () -> Must.isInstanceOf(String.class, null, "null不是任何类型的实例"));
        
        // 测试基本类型和包装类型的兼容性
        assertDoesNotThrow(() -> Must.isInstanceOf(Integer.class, 1, "整数应该是Integer的实例"));
        assertDoesNotThrow(() -> Must.isAssignable(Number.class, Integer.class, "Integer应该可以赋值给Number"));
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