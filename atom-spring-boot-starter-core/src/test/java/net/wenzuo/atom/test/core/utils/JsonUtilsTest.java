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

package net.wenzuo.atom.test.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.util.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
public class JsonUtilsTest {

    @Data
    public static class User {

        private String name;
        private int age;
        private LocalDate birthDate;
        private LocalDateTime lastLogin;
        private LocalTime lastActivity;
        private BigDecimal balance;

    }

    private static class NestedUser {

        private User user;
        private String role;

        // Getters and setters
        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

    }

    @BeforeEach
    public void setUp() {
        // 如果需要，可以在此处进行任何设置
    }

    @Test
    public void toObject_EmptyJson_ReturnsNull() {
        User user = JsonUtils.toObject("", User.class);
        assertNull(user);
    }

    @Test
    public void toObject_ValidJson_ReturnsObject() {
        String json = "{\"name\":\"John\", \"age\":30, \"birthDate\":\"2000-01-01\", \"lastLogin\":\"2023-10-01 12:00:00\", \"lastActivity\":\"12:00:00\", \"balance\":100.50}";
        User user = JsonUtils.toObject(json, User.class);
        assertNotNull(user);
        assertEquals("John", user.getName());
        assertEquals(30, user.getAge());
        assertEquals(LocalDate.of(2000, 1, 1), user.getBirthDate());
        assertEquals(LocalDateTime.of(2023, 10, 1, 12, 0, 0), user.getLastLogin());
        assertEquals(LocalTime.of(12, 0, 0), user.getLastActivity());
        assertEquals(new BigDecimal("100.50"), user.getBalance());
    }

    @Test
    public void toObject_InvalidJson_ThrowsException() {
        String invalidJson = "{\"name\":\"John\", \"age\":30"; // 缺少闭合大括号
        assertThrows(RuntimeException.class, () -> JsonUtils.toObject(invalidJson, User.class));
    }

    @Test
    public void toObject_CharSequence_ReturnsJsonString() {
        String json = "This is a JSON string";
        String result = JsonUtils.toObject(json, String.class);
        assertEquals(json, result);
    }

    @Test
    public void toObject_NestedObject_ReturnsNestedObject() {
        String json = "{\"user\":{\"name\":\"John\", \"age\":30}, \"role\":\"admin\"}";
        NestedUser nestedUser = JsonUtils.toObject(json, NestedUser.class);
        assertNotNull(nestedUser);
        assertNotNull(nestedUser.getUser());
        assertEquals("John", nestedUser.getUser().getName());
        assertEquals(30, nestedUser.getUser().getAge());
        assertEquals("admin", nestedUser.getRole());
    }

    @Test
    public void toObject_Array_ReturnsArray() {
        String json = "[{\"name\":\"John\", \"age\":30}, {\"name\":\"Jane\", \"age\":25}]";
        List<User> users = JsonUtils.toObject(json, new TypeReference<List<User>>() {
        });
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("John", users.get(0).getName());
        assertEquals(30, users.get(0).getAge());
        assertEquals("Jane", users.get(1).getName());
        assertEquals(25, users.get(1).getAge());
    }

    @Test
    public void toObject_DateTime_ReturnsDateTime() {
        String json = "{\"birthDate\":\"2000-01-01\", \"lastLogin\":\"2023-10-01 12:00:00\", \"lastActivity\":\"12:00:00\"}";
        User user = JsonUtils.toObject(json, User.class);
        assertNotNull(user);
        assertEquals(LocalDate.of(2000, 1, 1), user.getBirthDate());
        assertEquals(LocalDateTime.of(2023, 10, 1, 12, 0, 0), user.getLastLogin());
        assertEquals(LocalTime.of(12, 0, 0), user.getLastActivity());
    }

    @Test
    public void toObject_InputStream_ReturnsObject() {
        String json = "{\"name\":\"John\", \"age\":30, \"birthDate\":\"2000-01-01\", \"lastLogin\":\"2023-10-01 12:00:00\", \"lastActivity\":\"12:00:00\", \"balance\":100.50}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        User user = JsonUtils.toObject(inputStream, User.class);
        assertNotNull(user);
        assertEquals("John", user.getName());
        assertEquals(30, user.getAge());
        assertEquals(LocalDate.of(2000, 1, 1), user.getBirthDate());
        assertEquals(LocalDateTime.of(2023, 10, 1, 12, 0, 0), user.getLastLogin());
        assertEquals(LocalTime.of(12, 0, 0), user.getLastActivity());
        assertEquals(new BigDecimal("100.50"), user.getBalance());
    }

    @Test
    public void toObject_InputStream_CharSequence_ReturnsJsonString() {
        String json = "This is a JSON string";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        String result = JsonUtils.toObject(inputStream, String.class);
        assertEquals(json, result);
    }

    @Test
    public void toObject_InputStream_Null_ReturnsNull() {
        User user = JsonUtils.toObject((InputStream) null, User.class);
        assertNull(user);
    }

    @Test
    public void toObject_GenericClass_ReturnsObject() {
        String json = "{\"name\":\"John\", \"age\":30}";
        User user = JsonUtils.toObject(json, User.class);
        assertNotNull(user);
        assertEquals("John", user.getName());
        assertEquals(30, user.getAge());
    }

    @Test
    public void toObject_GenericClass_Array_ReturnsArray() {
        String json = "[{\"name\":\"John\", \"age\":30}, {\"name\":\"Jane\", \"age\":25}]";
        List<User> users = JsonUtils.toObject(json, List.class, User.class);
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("John", users.get(0).getName());
        assertEquals(30, users.get(0).getAge());
        assertEquals("Jane", users.get(1).getName());
        assertEquals(25, users.get(1).getAge());
    }

    @Test
    public void toObject_GenericClass_InputStream_ReturnsObject() {
        String json = "{\"name\":\"John\", \"age\":30}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        User user = JsonUtils.toObject(inputStream, User.class);
        assertNotNull(user);
        assertEquals("John", user.getName());
        assertEquals(30, user.getAge());
    }

    @Test
    public void toObject_GenericClass_InputStream_Array_ReturnsArray() {
        String json = "[{\"name\":\"John\", \"age\":30}, {\"name\":\"Jane\", \"age\":25}]";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        List<User> users = JsonUtils.toObject(inputStream, List.class, User.class);
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("John", users.get(0).getName());
        assertEquals(30, users.get(0).getAge());
        assertEquals("Jane", users.get(1).getName());
        assertEquals(25, users.get(1).getAge());
    }

    @Test
    public void toObject_GenericClass_Type_ReturnsObject() {
        String json = "{\"name\":\"John\", \"age\":30}";
        Type type = new TypeReference<User>() {
        }.getType();
        User user = JsonUtils.toObject(json, type);
        assertNotNull(user);
        assertEquals("John", user.getName());
        assertEquals(30, user.getAge());
    }

    @Test
    public void toObject_GenericClass_Type_InputStream_ReturnsObject() {
        String json = "{\"name\":\"John\", \"age\":30}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        Type type = new TypeReference<User>() {
        }.getType();
        User user = JsonUtils.toObject(inputStream, type);
        assertNotNull(user);
        assertEquals("John", user.getName());
        assertEquals(30, user.getAge());
    }

    @Test
    public void toObject_GenericClass_TypeReference_ReturnsObject() {
        String json = "{\"name\":\"John\", \"age\":30}";
        TypeReference<User> typeReference = new TypeReference<User>() {
        };
        User user = JsonUtils.toObject(json, typeReference);
        assertNotNull(user);
        assertEquals("John", user.getName());
        assertEquals(30, user.getAge());
    }

    @Test
    public void toObject_GenericClass_TypeReference_InputStream_ReturnsObject() {
        String json = "{\"name\":\"John\", \"age\":30}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        TypeReference<User> typeReference = new TypeReference<User>() {
        };
        User user = JsonUtils.toObject(inputStream, typeReference);
        assertNotNull(user);
        assertEquals("John", user.getName());
        assertEquals(30, user.getAge());
    }

    @Test
    public void toJson_NullObject_ReturnsNull() {
        String json = JsonUtils.toJson(null);
        assertNull(json);
    }

    @Test
    public void toJson_CharSequence_ReturnsJsonString() {
        String json = JsonUtils.toJson("This is a JSON string");
        assertEquals("This is a JSON string", json);
    }

    @Test
    public void toJson_Number_ReturnsJsonString() {
        String json = JsonUtils.toJson(123);
        assertEquals("123", json);
    }

    @Test
    public void toJson_Object_ReturnsJsonString() {
        User user = new User();
        user.setName("John");
        user.setAge(30);
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setLastLogin(LocalDateTime.of(2023, 10, 1, 12, 0, 0));
        user.setLastActivity(LocalTime.of(12, 0, 0));
        user.setBalance(new BigDecimal("100.50"));

        String json = JsonUtils.toJson(user);
        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"John\""));
        assertTrue(json.contains("\"age\":30"));
        assertTrue(json.contains("\"birthDate\":\"2000-01-01\""));
        assertTrue(json.contains("\"lastLogin\":\"2023-10-01 12:00:00\""));
        assertTrue(json.contains("\"lastActivity\":\"12:00:00\""));
        assertTrue(json.contains("\"balance\":\"100.50\""));
    }

    @Test
    public void toJson_NestedObject_ReturnsJsonString() {
        User user = new User();
        user.setName("John");
        user.setAge(30);

        NestedUser nestedUser = new NestedUser();
        nestedUser.setUser(user);
        nestedUser.setRole("admin");

        String json = JsonUtils.toJson(nestedUser);
        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"John\""));
        assertTrue(json.contains("\"age\":30"));
        assertTrue(json.contains("\"role\":\"admin\""));
    }

    @Test
    public void toJson_Array_ReturnsJsonString() {
        User user1 = new User();
        user1.setName("John");
        user1.setAge(30);

        User user2 = new User();
        user2.setName("Jane");
        user2.setAge(25);

        List<User> users = Arrays.asList(user1, user2);

        String json = JsonUtils.toJson(users);
        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"John\""));
        assertTrue(json.contains("\"age\":30"));
        assertTrue(json.contains("\"name\":\"Jane\""));
        assertTrue(json.contains("\"age\":25"));
    }

    @Test
    public void tsToLocalDateTime() {
        long t = System.currentTimeMillis();
        log.info("t: {}", t);
        LocalDateTime time = JsonUtils.toObject(String.valueOf(t), LocalDateTime.class);
        log.info("time: {}", time);

        int ts = (int) (t / 1000);
        log.info("ts: {}", ts);
        time = JsonUtils.toObject(String.valueOf(ts), LocalDateTime.class);
        log.info("time: {}", time);
    }

}