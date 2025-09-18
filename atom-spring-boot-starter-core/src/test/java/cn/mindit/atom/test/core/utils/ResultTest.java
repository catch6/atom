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

import cn.mindit.atom.core.util.Result;
import cn.mindit.atom.core.util.ResultProvider;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Result 工具类测试
 *
 * @author Catch
 * @since 2023-08-08
 */
class ResultTest {

    @Test
    @DisplayName("测试 ok() 方法 - 无参数")
    void testOkWithoutParameters() {
        Result<String> result = Result.ok();
        
        assertEquals(200, result.getCode());
        assertEquals("成功", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("测试 ok() 方法 - 带数据")
    void testOkWithData() {
        String data = "测试数据";
        Result<String> result = Result.ok(data);
        
        assertEquals(200, result.getCode());
        assertEquals("成功", result.getMessage());
        assertEquals(data, result.getData());
    }

    @Test
    @DisplayName("测试 ok() 方法 - 带复杂数据")
    void testOkWithComplexData() {
        TestData testData = new TestData("张三", 25);
        Result<TestData> result = Result.ok(testData);
        
        assertEquals(200, result.getCode());
        assertEquals("成功", result.getMessage());
        assertNotNull(result.getData());
        assertEquals("张三", result.getData().getName());
        assertEquals(25, result.getData().getAge());
    }

    @Test
    @DisplayName("测试 ok() 方法 - 带null数据")
    void testOkWithNullData() {
        Result<String> result = Result.ok(null);
        
        assertEquals(200, result.getCode());
        assertEquals("成功", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("测试 fail() 方法 - 带代码和消息")
    void testFailWithCodeAndMessage() {
        int code = 400;
        String message = "请求参数错误";
        Result<String> result = Result.fail(code, message);
        
        assertEquals(code, result.getCode());
        assertEquals(message, result.getMessage());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("测试 fail() 方法 - 带ResultProvider")
    void testFailWithResultProvider() {
        TestResultProvider provider = new TestResultProvider(500, "服务器内部错误");
        Result<String> result = Result.fail(provider);
        
        assertEquals(provider.getCode(), result.getCode());
        assertEquals(provider.getMessage(), result.getMessage());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("测试 fail() 方法 - 边界值")
    void testFailWithEdgeCases() {
        // 测试最小代码值
        Result<String> result1 = Result.fail(Integer.MIN_VALUE, "最小代码");
        assertEquals(Integer.MIN_VALUE, result1.getCode());
        assertEquals("最小代码", result1.getMessage());
        
        // 测试最大代码值
        Result<String> result2 = Result.fail(Integer.MAX_VALUE, "最大代码");
        assertEquals(Integer.MAX_VALUE, result2.getCode());
        assertEquals("最大代码", result2.getMessage());
        
        // 测试空消息
        Result<String> result3 = Result.fail(400, "");
        assertEquals(400, result3.getCode());
        assertEquals("", result3.getMessage());
        
        // 测试null消息
        Result<String> result4 = Result.fail(400, null);
        assertEquals(400, result4.getCode());
        assertNull(result4.getMessage());
    }

    @Test
    @DisplayName("测试 Result 的 equals 和 hashCode")
    void testEqualsAndHashCode() {
        TestData data = new TestData("张三", 25);
        
        Result<TestData> result1 = Result.ok(data);
        Result<TestData> result2 = Result.ok(data);
        Result<TestData> result3 = Result.fail(400, "错误");
        
        // 相同的结果应该相等
        assertEquals(result1, result2);
        assertEquals(result1.hashCode(), result2.hashCode());
        
        // 不同的结果不应该相等
        assertNotEquals(result1, result3);
        assertNotEquals(result1.hashCode(), result3.hashCode());
        
        // 与null比较
        assertNotEquals(null, result1);
        
        // 与不同类型比较
        assertNotEquals(result1, "字符串");
    }

    @Test
    @DisplayName("测试 Result 的 toString")
    void testToString() {
        Result<String> result = Result.ok("测试数据");
        String toString = result.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("code=200"));
        assertTrue(toString.contains("message=成功"));
        assertTrue(toString.contains("data=测试数据"));
    }

    @Test
    @DisplayName("测试 Result 的 getter 和 setter")
    void testGettersAndSetters() {
        Result<String> result = new Result<>();
        
        // 测试初始值
        assertEquals(0, result.getCode());
        assertNull(result.getMessage());
        assertNull(result.getData());
        
        // 测试设置值
        result.setCode(200);
        result.setMessage("成功");
        result.setData("测试数据");
        
        assertEquals(200, result.getCode());
        assertEquals("成功", result.getMessage());
        assertEquals("测试数据", result.getData());
    }

    @Test
    @DisplayName("测试 Result 的构造函数")
    void testConstructors() {
        // 测试无参构造函数
        Result<String> result1 = new Result<>();
        assertEquals(0, result1.getCode());
        assertNull(result1.getMessage());
        assertNull(result1.getData());
        
        // 测试全参构造函数
        Result<String> result2 = new Result<>(200, "成功", "数据");
        assertEquals(200, result2.getCode());
        assertEquals("成功", result2.getMessage());
        assertEquals("数据", result2.getData());
        
        // 测试部分参数构造函数
        Result<String> result3 = new Result<>(400, "错误", null);
        assertEquals(400, result3.getCode());
        assertEquals("错误", result3.getMessage());
        assertNull(result3.getData());
    }

    @Test
    @DisplayName("测试 Result 的泛型类型安全")
    void testGenericTypeSafety() {
        // 测试字符串类型
        Result<String> stringResult = Result.ok("字符串");
        assertEquals("字符串", stringResult.getData());
        
        // 测试整数类型
        Result<Integer> integerResult = Result.ok(123);
        assertEquals(123, integerResult.getData());
        
        // 测试自定义对象类型
        TestData data = new TestData("张三", 25);
        Result<TestData> objectResult = Result.ok(data);
        assertEquals(data, objectResult.getData());
        
        // 测试集合类型
        Result<java.util.List<String>> listResult = Result.ok(java.util.List.of("A", "B", "C"));
        assertEquals(3, listResult.getData().size());
    }

    @Test
    @DisplayName("测试 Result 的设置方法")
    void testSetMethods() {
        Result<String> result = new Result<>();
        
        // 测试设置方法
        result.setCode(200);
        result.setMessage("成功");
        result.setData("测试数据");
        
        assertEquals(200, result.getCode());
        assertEquals("成功", result.getMessage());
        assertEquals("测试数据", result.getData());
    }

    // 测试用的内部类
    @Data
    static class TestData {
        private String name;
        private int age;
        
        public TestData(String name, int age) {
            this.name = name;
            this.age = age;
        }
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