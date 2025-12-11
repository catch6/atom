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

import cn.mindit.atom.core.util.JsonUtils;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JsonUtils 工具类测试
 *
 * @author Catch
 * @since 2023-08-08
 */
class JsonUtilsTest {

    @Data
    static class TestUser {

        private String name;
        private Integer age;
        private LocalDateTime createTime;
        private BigDecimal salary;

    }

    @Data
    static class TestResult<T> {

        private Boolean success;
        private String message;
        private T data;

    }

    @Test
    @DisplayName("测试 toJson 方法 - 基本对象")
    void testToJsonWithBasicObject() {
        TestUser user = new TestUser();
        user.setName("张三");
        user.setAge(25);
        user.setCreateTime(LocalDateTime.now());
        user.setSalary(new BigDecimal("5000.00"));

        String json = JsonUtils.toJson(user);
        assertNotNull(json);
        assertTrue(json.contains("张三"));
        assertTrue(json.contains("25"));
    }

    @Test
    @DisplayName("测试 toJson 方法 - null 值")
    void testToJsonWithNull() {
        String json = JsonUtils.toJson(null);
        assertNull(json);
    }

    @Test
    @DisplayName("测试 toJson 方法 - 字符串")
    void testToJsonWithString() {
        String text = "Hello World";
        String json = JsonUtils.toJson(text);
        assertEquals(text, json);
    }

    @Test
    @DisplayName("测试 toJson 方法 - 数字")
    void testToJsonWithNumber() {
        Integer number = 123;
        String json = JsonUtils.toJson(number);
        assertEquals("123", json);
    }

    @Test
    @DisplayName("测试 toPrettyJson 方法")
    void testToPrettyJson() {
        TestUser user = new TestUser();
        user.setName("张三");
        user.setAge(25);

        String json = JsonUtils.toPrettyJson(user);
        assertNotNull(json);
        assertTrue(json.contains("张三"));
        assertTrue(json.contains("\n")); // 格式化应该包含换行符
    }

    @Test
    @DisplayName("测试 toObject 方法 - 基本类型")
    void testToObjectWithBasicType() {
        String json = "{\"name\":\"张三\",\"age\":25}";
        TestUser user = JsonUtils.toObject(json, TestUser.class);

        assertNotNull(user);
        assertEquals("张三", user.getName());
        assertEquals(25, user.getAge());
    }

    @Test
    @DisplayName("测试 toObject 方法 - 空字符串")
    void testToObjectWithEmptyString() {
        TestUser user = JsonUtils.toObject("", TestUser.class);
        assertNull(user);
    }

    @Test
    @DisplayName("测试 toObject 方法 - null 值")
    void testToObjectWithNullString() {
        TestUser user = JsonUtils.toObject((String) null, TestUser.class);
        assertNull(user);
    }

    @Test
    @DisplayName("测试 toObject 方法 - 字符串类型")
    void testToObjectWithStringType() {
        String text = "Hello World";
        String result = JsonUtils.toObject(text, String.class);
        assertEquals(text, result);
    }

    @Test
    @DisplayName("测试 toObject 方法 - 输入流")
    void testToObjectWithInputStream() throws Exception {
        String json = "{\"name\":\"张三\",\"age\":25}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());

        TestUser user = JsonUtils.toObject(inputStream, TestUser.class);
        assertNotNull(user);
        assertEquals("张三", user.getName());
        assertEquals(25, user.getAge());
    }

    @Test
    @DisplayName("测试 toObject 方法 - 泛型包装类")
    void testToObjectWithWrapperType() {
        String json = "{\"success\":true,\"message\":\"操作成功\",\"data\":{\"name\":\"张三\",\"age\":25}}";
        TestResult<TestUser> result = JsonUtils.toObject(json, TestResult.class, TestUser.class);

        assertNotNull(result);
        assertTrue(result.getSuccess());
        assertEquals("操作成功", result.getMessage());
        assertNotNull(result.getData());
        assertEquals("张三", result.getData().getName());
        assertEquals(25, result.getData().getAge());
    }

    @Test
    @DisplayName("测试 toObject 方法 - Type 类型")
    void testToObjectWithJavaType() {
        String json = "[{\"name\":\"张三\",\"age\":25},{\"name\":\"李四\",\"age\":30}]";
        List<TestUser> users = JsonUtils.toObject(json, new TypeReference<>() {
        });

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("张三", users.get(0).getName());
        assertEquals("李四", users.get(1).getName());
    }

    @Test
    @DisplayName("测试 toObject 方法 - TypeReference 类型")
    void testToObjectWithTypeReference() {
        String json = "{\"total\":10,\"users\":[{\"name\":\"张三\",\"age\":25}]}";
        Map<String, Object> result = JsonUtils.toObject(json, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals(10, result.get("total"));
        assertNotNull(result.get("users"));
    }

    @Test
    @DisplayName("测试 toObject 方法 - 输入流和 TypeReference")
    void testToObjectWithInputStreamAndTypeReference() throws Exception {
        String json = "[{\"name\":\"张三\",\"age\":25}]";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());

        List<TestUser> users = JsonUtils.toObject(inputStream, new TypeReference<>() {
        });
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("张三", users.get(0).getName());
    }

    @Test
    @DisplayName("测试 objectMapper 方法")
    void testJsonMapper() {
        tools.jackson.databind.json.JsonMapper mapper = JsonUtils.jsonMapper();
        assertNotNull(mapper);

        // 测试序列化和反序列化
        TestUser user = new TestUser();
        user.setName("张三");
        user.setAge(25);

        try {
            String json = mapper.writeValueAsString(user);
            TestUser parsedUser = mapper.readValue(json, TestUser.class);
            assertEquals(user.getName(), parsedUser.getName());
            assertEquals(user.getAge(), parsedUser.getAge());
        } catch (Exception e) {
            fail("JsonMapper 序列化/反序列化失败: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("测试 customize 方法")
    void testCustomize() {
        JsonMapperBuilderCustomizer customizer = JsonUtils.customize();
        assertNotNull(customizer);

        // 测试自定义配置是否生效
        JsonMapper.Builder builder = JsonMapper.builder();
        customizer.customize(builder);
        JsonMapper jsonMapper = builder.build();

        assertNotNull(jsonMapper);
    }

    @Test
    @DisplayName("测试日期时间序列化")
    void testDateTimeSerialization() {
        TestUser user = new TestUser();
        user.setName("张三");
        user.setAge(25);
        user.setCreateTime(LocalDateTime.of(2023, 8, 8, 15, 30, 45));

        String json = JsonUtils.toJson(user);
        assertNotNull(json);
        assertTrue(json.contains("2023-08-08 15:30:45"));

        // 反序列化验证
        TestUser parsedUser = JsonUtils.toObject(json, TestUser.class);
        assertEquals(user.getCreateTime(), parsedUser.getCreateTime());
    }

    @Test
    @DisplayName("测试 BigDecimal 序列化")
    void testBigDecimalSerialization() {
        TestUser user = new TestUser();
        user.setName("张三");
        user.setAge(25);
        user.setSalary(new BigDecimal("5000.123456"));

        String json = JsonUtils.toJson(user);
        assertNotNull(json);

        TestUser parsedUser = JsonUtils.toObject(json, TestUser.class);
        assertEquals(user.getSalary(), parsedUser.getSalary());
    }

    @Test
    @DisplayName("测试异常情况 - 无效 JSON")
    void testInvalidJson() {
        String invalidJson = "{invalid json}";

        assertThrows(RuntimeException.class, () -> {
            JsonUtils.toObject(invalidJson, TestUser.class);
        });
    }

    @Test
    @DisplayName("测试复杂嵌套对象")
    void testComplexNestedObject() {
        TestUser user = new TestUser();
        user.setName("张三");
        user.setAge(25);
        user.setCreateTime(LocalDateTime.now());
        user.setSalary(new BigDecimal("5000.00"));

        TestResult<TestUser> result = new TestResult<>();
        result.setSuccess(true);
        result.setMessage("操作成功");
        result.setData(user);

        String json = JsonUtils.toJson(result);
        assertNotNull(json);
        assertTrue(json.contains("张三"));
        assertTrue(json.contains("操作成功"));

        TestResult<TestUser> parsedResult = JsonUtils.toObject(json,
            new TypeReference<>() {
            });
        assertNotNull(parsedResult);
        assertTrue(parsedResult.getSuccess());
        assertEquals("张三", parsedResult.getData().getName());
    }

    @Test
    @DisplayName("测试 List 类型处理")
    void testListTypeHandling() {
        List<TestUser> users = List.of(
            createTestUser("张三", 25),
            createTestUser("李四", 30)
        );

        String json = JsonUtils.toJson(users);
        assertNotNull(json);

        List<TestUser> parsedUsers = JsonUtils.toObject(json, new TypeReference<>() {
        });
        assertEquals(2, parsedUsers.size());
        assertEquals("张三", parsedUsers.get(0).getName());
        assertEquals("李四", parsedUsers.get(1).getName());
    }

    private TestUser createTestUser(String name, int age) {
        TestUser user = new TestUser();
        user.setName(name);
        user.setAge(age);
        user.setCreateTime(LocalDateTime.now());
        user.setSalary(new BigDecimal("5000.00"));
        return user;
    }

}