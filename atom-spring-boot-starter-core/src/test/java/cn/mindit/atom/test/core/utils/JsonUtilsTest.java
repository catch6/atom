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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
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
    @NoArgsConstructor
    @AllArgsConstructor
    static class SimpleObject {
        private String name;
        private Integer age;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class DateTimeObject {
        private LocalDate localDate;
        private LocalTime localTime;
        private LocalDateTime localDateTime;
        private Date date;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class NumberObject {
        private Integer intValue;
        private Long longValue;
        private BigDecimal bigDecimalValue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class GenericWrapper<T> {
        private T data;
        private String message;
    }

    @Nested
    @DisplayName("toJson 方法测试")
    class ToJsonTest {

        @Test
        @DisplayName("测试 null 值转换")
        void testNullValue() {
            assertNull(JsonUtils.toJson(null));
        }

        @Test
        @DisplayName("测试 CharSequence 类型转换")
        void testCharSequenceValue() {
            assertEquals("hello", JsonUtils.toJson("hello"));
            assertEquals("test string", JsonUtils.toJson(new StringBuilder("test string")));
        }

        @Test
        @DisplayName("测试 Number 类型转换")
        void testNumberValue() {
            assertEquals("123", JsonUtils.toJson(123));
            assertEquals("456", JsonUtils.toJson(456L));
            assertEquals("3.14", JsonUtils.toJson(3.14));
            assertEquals("100.50", JsonUtils.toJson(new BigDecimal("100.50")));
        }

        @Test
        @DisplayName("测试简单对象转换")
        void testSimpleObject() {
            SimpleObject obj = new SimpleObject("张三", 25);
            String json = JsonUtils.toJson(obj);

            assertNotNull(json);
            assertTrue(json.contains("\"name\":\"张三\""));
            assertTrue(json.contains("\"age\":25"));
        }

        @Test
        @DisplayName("测试 List 转换")
        void testListConversion() {
            List<SimpleObject> list = List.of(
                    new SimpleObject("张三", 25),
                    new SimpleObject("李四", 30)
            );
            String json = JsonUtils.toJson(list);

            assertNotNull(json);
            assertTrue(json.startsWith("["));
            assertTrue(json.endsWith("]"));
            assertTrue(json.contains("张三"));
            assertTrue(json.contains("李四"));
        }

        @Test
        @DisplayName("测试 Map 转换")
        void testMapConversion() {
            Map<String, Object> map = Map.of(
                    "key1", "value1",
                    "key2", 123
            );
            String json = JsonUtils.toJson(map);

            assertNotNull(json);
            assertTrue(json.contains("\"key1\":\"value1\""));
            assertTrue(json.contains("\"key2\":123"));
        }

        @Test
        @DisplayName("测试日期时间类型转换")
        void testDateTimeConversion() {
            DateTimeObject obj = new DateTimeObject();
            obj.setLocalDate(LocalDate.of(2023, 8, 8));
            obj.setLocalTime(LocalTime.of(10, 30, 45));
            obj.setLocalDateTime(LocalDateTime.of(2023, 8, 8, 10, 30, 45));

            String json = JsonUtils.toJson(obj);

            assertNotNull(json);
            assertTrue(json.contains("\"localDate\":\"2023-08-08\""));
            assertTrue(json.contains("\"localTime\":\"10:30:45\""));
            assertTrue(json.contains("\"localDateTime\":\"2023-08-08 10:30:45\""));
        }
    }

    @Nested
    @DisplayName("toPrettyJson 方法测试")
    class ToPrettyJsonTest {

        @Test
        @DisplayName("测试 null 值转换")
        void testNullValue() {
            assertNull(JsonUtils.toPrettyJson(null));
        }

        @Test
        @DisplayName("测试 CharSequence 类型转换")
        void testCharSequenceValue() {
            assertEquals("hello", JsonUtils.toPrettyJson("hello"));
        }

        @Test
        @DisplayName("测试 Number 类型转换")
        void testNumberValue() {
            assertEquals("123", JsonUtils.toPrettyJson(123));
        }

        @Test
        @DisplayName("测试格式化输出")
        void testPrettyFormat() {
            SimpleObject obj = new SimpleObject("张三", 25);
            String json = JsonUtils.toPrettyJson(obj);

            assertNotNull(json);
            assertTrue(json.contains("\n"));
            assertTrue(json.contains("  "));
        }
    }

    @Nested
    @DisplayName("toObject(String, Class) 方法测试")
    class ToObjectWithClassTest {

        @Test
        @DisplayName("测试 null 值转换")
        void testNullValue() {
            assertNull(JsonUtils.toObject((String) null, SimpleObject.class));
        }

        @Test
        @DisplayName("测试空字符串转换")
        void testEmptyString() {
            assertNull(JsonUtils.toObject("", SimpleObject.class));
        }

        @Test
        @DisplayName("测试 CharSequence 类型直接返回")
        void testCharSequenceType() {
            String json = "{\"name\":\"张三\"}";
            String result = JsonUtils.toObject(json, String.class);
            assertEquals(json, result);
        }

        @Test
        @DisplayName("测试简单对象转换")
        void testSimpleObject() {
            String json = "{\"name\":\"张三\",\"age\":25}";
            SimpleObject obj = JsonUtils.toObject(json, SimpleObject.class);

            assertNotNull(obj);
            assertEquals("张三", obj.getName());
            assertEquals(25, obj.getAge());
        }

        @Test
        @DisplayName("测试 List 转换")
        void testListConversion() {
            String json = "[{\"name\":\"张三\",\"age\":25},{\"name\":\"李四\",\"age\":30}]";
            List<?> list = JsonUtils.toObject(json, List.class);

            assertNotNull(list);
            assertEquals(2, list.size());
        }

        @Test
        @DisplayName("测试 Map 转换")
        void testMapConversion() {
            String json = "{\"key1\":\"value1\",\"key2\":123}";
            Map<?, ?> map = JsonUtils.toObject(json, Map.class);

            assertNotNull(map);
            assertEquals("value1", map.get("key1"));
            assertEquals(123, map.get("key2"));
        }
    }

    @Nested
    @DisplayName("toObject(InputStream, Class) 方法测试")
    class ToObjectWithInputStreamAndClassTest {

        @Test
        @DisplayName("测试 null InputStream")
        void testNullInputStream() {
            assertNull(JsonUtils.toObject((InputStream) null, SimpleObject.class));
        }

        @Test
        @DisplayName("测试 CharSequence 类型直接读取")
        void testCharSequenceType() {
            String json = "{\"name\":\"张三\"}";
            InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            String result = JsonUtils.toObject(inputStream, String.class);
            assertEquals(json, result);
        }

        @Test
        @DisplayName("测试简单对象转换")
        void testSimpleObject() {
            String json = "{\"name\":\"张三\",\"age\":25}";
            InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            SimpleObject obj = JsonUtils.toObject(inputStream, SimpleObject.class);

            assertNotNull(obj);
            assertEquals("张三", obj.getName());
            assertEquals(25, obj.getAge());
        }
    }

    @Nested
    @DisplayName("toObject(String, Class, Class...) 泛型方法测试")
    class ToObjectWithGenericTest {

        @Test
        @DisplayName("测试 null 值转换")
        void testNullValue() {
            assertNull(JsonUtils.toObject((String) null, List.class, SimpleObject.class));
        }

        @Test
        @DisplayName("测试空字符串转换")
        void testEmptyString() {
            assertNull(JsonUtils.toObject("", List.class, SimpleObject.class));
        }

        @Test
        @DisplayName("测试 List<SimpleObject> 转换")
        void testListGeneric() {
            String json = "[{\"name\":\"张三\",\"age\":25},{\"name\":\"李四\",\"age\":30}]";
            List<SimpleObject> list = JsonUtils.toObject(json, List.class, SimpleObject.class);

            assertNotNull(list);
            assertEquals(2, list.size());
            assertEquals("张三", list.get(0).getName());
            assertEquals("李四", list.get(1).getName());
        }

        @Test
        @DisplayName("测试 Map<String, SimpleObject> 转换")
        void testMapGeneric() {
            String json = "{\"user1\":{\"name\":\"张三\",\"age\":25},\"user2\":{\"name\":\"李四\",\"age\":30}}";
            Map<String, SimpleObject> map = JsonUtils.toObject(json, Map.class, String.class, SimpleObject.class);

            assertNotNull(map);
            assertEquals(2, map.size());
            assertEquals("张三", map.get("user1").getName());
            assertEquals("李四", map.get("user2").getName());
        }
    }

    @Nested
    @DisplayName("toObject(InputStream, Class, Class...) 泛型方法测试")
    class ToObjectWithInputStreamAndGenericTest {

        @Test
        @DisplayName("测试 null InputStream")
        void testNullInputStream() {
            assertNull(JsonUtils.toObject((InputStream) null, List.class, SimpleObject.class));
        }

        @Test
        @DisplayName("测试 List<SimpleObject> 转换")
        void testListGeneric() {
            String json = "[{\"name\":\"张三\",\"age\":25},{\"name\":\"李四\",\"age\":30}]";
            InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            List<SimpleObject> list = JsonUtils.toObject(inputStream, List.class, SimpleObject.class);

            assertNotNull(list);
            assertEquals(2, list.size());
            assertEquals("张三", list.get(0).getName());
        }
    }

    @Nested
    @DisplayName("toObject(String, Type) 方法测试")
    class ToObjectWithTypeTest {

        @Test
        @DisplayName("测试 null 值转换")
        void testNullValue() {
            assertNull(JsonUtils.toObject((String) null, SimpleObject.class));
        }

        @Test
        @DisplayName("测试空字符串转换")
        void testEmptyString() {
            assertNull(JsonUtils.toObject("", SimpleObject.class));
        }

        @Test
        @DisplayName("测试简单对象转换")
        void testSimpleObject() {
            String json = "{\"name\":\"张三\",\"age\":25}";
            SimpleObject obj = JsonUtils.toObject(json, (java.lang.reflect.Type) SimpleObject.class);

            assertNotNull(obj);
            assertEquals("张三", obj.getName());
            assertEquals(25, obj.getAge());
        }
    }

    @Nested
    @DisplayName("toObject(InputStream, Type) 方法测试")
    class ToObjectWithInputStreamAndTypeTest {

        @Test
        @DisplayName("测试 null InputStream")
        void testNullInputStream() {
            assertNull(JsonUtils.toObject((InputStream) null, (java.lang.reflect.Type) SimpleObject.class));
        }

        @Test
        @DisplayName("测试简单对象转换")
        void testSimpleObject() {
            String json = "{\"name\":\"张三\",\"age\":25}";
            InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            SimpleObject obj = JsonUtils.toObject(inputStream, (java.lang.reflect.Type) SimpleObject.class);

            assertNotNull(obj);
            assertEquals("张三", obj.getName());
            assertEquals(25, obj.getAge());
        }
    }

    @Nested
    @DisplayName("toObject(String, TypeReference) 方法测试")
    class ToObjectWithTypeReferenceTest {

        @Test
        @DisplayName("测试 null 值转换")
        void testNullValue() {
            assertNull(JsonUtils.toObject((String) null, new TypeReference<List<SimpleObject>>() {}));
        }

        @Test
        @DisplayName("测试空字符串转换")
        void testEmptyString() {
            assertNull(JsonUtils.toObject("", new TypeReference<List<SimpleObject>>() {}));
        }

        @Test
        @DisplayName("测试 List<SimpleObject> 转换")
        void testListTypeReference() {
            String json = "[{\"name\":\"张三\",\"age\":25},{\"name\":\"李四\",\"age\":30}]";
            List<SimpleObject> list = JsonUtils.toObject(json, new TypeReference<List<SimpleObject>>() {});

            assertNotNull(list);
            assertEquals(2, list.size());
            assertEquals("张三", list.get(0).getName());
            assertEquals("李四", list.get(1).getName());
        }

        @Test
        @DisplayName("测试 Map<String, SimpleObject> 转换")
        void testMapTypeReference() {
            String json = "{\"user1\":{\"name\":\"张三\",\"age\":25},\"user2\":{\"name\":\"李四\",\"age\":30}}";
            Map<String, SimpleObject> map = JsonUtils.toObject(json, new TypeReference<Map<String, SimpleObject>>() {});

            assertNotNull(map);
            assertEquals(2, map.size());
            assertEquals("张三", map.get("user1").getName());
        }
    }

    @Nested
    @DisplayName("toObject(InputStream, TypeReference) 方法测试")
    class ToObjectWithInputStreamAndTypeReferenceTest {

        @Test
        @DisplayName("测试 null InputStream")
        void testNullInputStream() {
            assertNull(JsonUtils.toObject((InputStream) null, new TypeReference<List<SimpleObject>>() {}));
        }

        @Test
        @DisplayName("测试 List<SimpleObject> 转换")
        void testListTypeReference() {
            String json = "[{\"name\":\"张三\",\"age\":25},{\"name\":\"李四\",\"age\":30}]";
            InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            List<SimpleObject> list = JsonUtils.toObject(inputStream, new TypeReference<List<SimpleObject>>() {});

            assertNotNull(list);
            assertEquals(2, list.size());
            assertEquals("张三", list.get(0).getName());
        }
    }

    @Nested
    @DisplayName("jsonMapper 方法测试")
    class JsonMapperTest {

        @Test
        @DisplayName("测试 jsonMapper 不为空")
        void testJsonMapperNotNull() {
            JsonMapper mapper = JsonUtils.jsonMapper();
            assertNotNull(mapper);
        }

        @Test
        @DisplayName("测试静态 jsonMapper 实例不为空")
        void testStaticJsonMapperNotNull() {
            assertNotNull(JsonUtils.jsonMapper);
        }

        @Test
        @DisplayName("测试每次调用 jsonMapper() 返回新实例")
        void testJsonMapperNewInstance() {
            JsonMapper mapper1 = JsonUtils.jsonMapper();
            JsonMapper mapper2 = JsonUtils.jsonMapper();
            assertNotSame(mapper1, mapper2);
        }
    }

    @Nested
    @DisplayName("customize 方法测试")
    class CustomizeTest {

        @Test
        @DisplayName("测试 customize 不为空")
        void testCustomizeNotNull() {
            assertNotNull(JsonUtils.customize());
        }
    }

    @Nested
    @DisplayName("日期时间序列化/反序列化测试")
    class DateTimeSerializationTest {

        @Test
        @DisplayName("测试 LocalDate 序列化格式")
        void testLocalDateSerialization() {
            DateTimeObject obj = new DateTimeObject();
            obj.setLocalDate(LocalDate.of(2023, 8, 8));

            String json = JsonUtils.toJson(obj);
            assertTrue(json.contains("\"localDate\":\"2023-08-08\""));
        }

        @Test
        @DisplayName("测试 LocalDate 反序列化")
        void testLocalDateDeserialization() {
            String json = "{\"localDate\":\"2023-08-08\"}";
            DateTimeObject obj = JsonUtils.toObject(json, DateTimeObject.class);

            assertNotNull(obj);
            assertEquals(LocalDate.of(2023, 8, 8), obj.getLocalDate());
        }

        @Test
        @DisplayName("测试 LocalTime 序列化格式")
        void testLocalTimeSerialization() {
            DateTimeObject obj = new DateTimeObject();
            obj.setLocalTime(LocalTime.of(10, 30, 45));

            String json = JsonUtils.toJson(obj);
            assertTrue(json.contains("\"localTime\":\"10:30:45\""));
        }

        @Test
        @DisplayName("测试 LocalTime 反序列化")
        void testLocalTimeDeserialization() {
            String json = "{\"localTime\":\"10:30:45\"}";
            DateTimeObject obj = JsonUtils.toObject(json, DateTimeObject.class);

            assertNotNull(obj);
            assertEquals(LocalTime.of(10, 30, 45), obj.getLocalTime());
        }

        @Test
        @DisplayName("测试 LocalDateTime 序列化格式")
        void testLocalDateTimeSerialization() {
            DateTimeObject obj = new DateTimeObject();
            obj.setLocalDateTime(LocalDateTime.of(2023, 8, 8, 10, 30, 45));

            String json = JsonUtils.toJson(obj);
            assertTrue(json.contains("\"localDateTime\":\"2023-08-08 10:30:45\""));
        }

        @Test
        @DisplayName("测试 LocalDateTime 反序列化")
        void testLocalDateTimeDeserialization() {
            String json = "{\"localDateTime\":\"2023-08-08 10:30:45\"}";
            DateTimeObject obj = JsonUtils.toObject(json, DateTimeObject.class);

            assertNotNull(obj);
            assertEquals(LocalDateTime.of(2023, 8, 8, 10, 30, 45), obj.getLocalDateTime());
        }

        @Test
        @DisplayName("测试 LocalDateTime 时间戳反序列化 - 毫秒级")
        void testLocalDateTimeTimestampMillisDeserialization() {
            long timestampMillis = 1691476245000L;
            String json = "{\"localDateTime\":" + timestampMillis + "}";
            DateTimeObject obj = JsonUtils.toObject(json, DateTimeObject.class);

            assertNotNull(obj);
            assertNotNull(obj.getLocalDateTime());
        }

        @Test
        @DisplayName("测试 LocalDateTime 时间戳反序列化 - 秒级")
        void testLocalDateTimeTimestampSecondsDeserialization() {
            long timestampSeconds = 1691476245L;
            String json = "{\"localDateTime\":" + timestampSeconds + "}";
            DateTimeObject obj = JsonUtils.toObject(json, DateTimeObject.class);

            assertNotNull(obj);
            assertNotNull(obj.getLocalDateTime());
        }
    }

    @Nested
    @DisplayName("数值类型序列化/反序列化测试")
    class NumberSerializationTest {

        @Test
        @DisplayName("测试 Integer 反序列化")
        void testIntegerDeserialization() {
            String json = "{\"intValue\":123}";
            NumberObject obj = JsonUtils.toObject(json, NumberObject.class);

            assertNotNull(obj);
            assertEquals(123, obj.getIntValue());
        }

        @Test
        @DisplayName("测试 Integer 从字符串反序列化")
        void testIntegerFromStringDeserialization() {
            String json = "{\"intValue\":\"456\"}";
            NumberObject obj = JsonUtils.toObject(json, NumberObject.class);

            assertNotNull(obj);
            assertEquals(456, obj.getIntValue());
        }

        @Test
        @DisplayName("测试 Long 反序列化")
        void testLongDeserialization() {
            String json = "{\"longValue\":123456789012345}";
            NumberObject obj = JsonUtils.toObject(json, NumberObject.class);

            assertNotNull(obj);
            assertEquals(123456789012345L, obj.getLongValue());
        }

        @Test
        @DisplayName("测试 Long 从字符串反序列化")
        void testLongFromStringDeserialization() {
            String json = "{\"longValue\":\"987654321098765\"}";
            NumberObject obj = JsonUtils.toObject(json, NumberObject.class);

            assertNotNull(obj);
            assertEquals(987654321098765L, obj.getLongValue());
        }

        @Test
        @DisplayName("测试 BigDecimal 序列化")
        void testBigDecimalSerialization() {
            NumberObject obj = new NumberObject();
            obj.setBigDecimalValue(new BigDecimal("123.456"));

            String json = JsonUtils.toJson(obj);
            assertNotNull(json);
            assertTrue(json.contains("bigDecimalValue"));
        }

        @Test
        @DisplayName("测试 BigDecimal 反序列化")
        void testBigDecimalDeserialization() {
            String json = "{\"bigDecimalValue\":123.456}";
            NumberObject obj = JsonUtils.toObject(json, NumberObject.class);

            assertNotNull(obj);
            assertEquals(new BigDecimal("123.456"), obj.getBigDecimalValue());
        }

        @Test
        @DisplayName("测试 BigDecimal 从字符串反序列化")
        void testBigDecimalFromStringDeserialization() {
            String json = "{\"bigDecimalValue\":\"789.012\"}";
            NumberObject obj = JsonUtils.toObject(json, NumberObject.class);

            assertNotNull(obj);
            assertEquals(new BigDecimal("789.012"), obj.getBigDecimalValue());
        }
    }

    @Nested
    @DisplayName("双向转换测试")
    class BidirectionalConversionTest {

        @Test
        @DisplayName("测试简单对象双向转换")
        void testSimpleObjectBidirectional() {
            SimpleObject original = new SimpleObject("张三", 25);

            String json = JsonUtils.toJson(original);
            SimpleObject converted = JsonUtils.toObject(json, SimpleObject.class);

            assertEquals(original.getName(), converted.getName());
            assertEquals(original.getAge(), converted.getAge());
        }

        @Test
        @DisplayName("测试日期时间对象双向转换")
        void testDateTimeObjectBidirectional() {
            DateTimeObject original = new DateTimeObject();
            original.setLocalDate(LocalDate.of(2023, 8, 8));
            original.setLocalTime(LocalTime.of(10, 30, 45));
            original.setLocalDateTime(LocalDateTime.of(2023, 8, 8, 10, 30, 45));

            String json = JsonUtils.toJson(original);
            DateTimeObject converted = JsonUtils.toObject(json, DateTimeObject.class);

            assertEquals(original.getLocalDate(), converted.getLocalDate());
            assertEquals(original.getLocalTime(), converted.getLocalTime());
            assertEquals(original.getLocalDateTime(), converted.getLocalDateTime());
        }

        @Test
        @DisplayName("测试数值对象双向转换")
        void testNumberObjectBidirectional() {
            NumberObject original = new NumberObject();
            original.setIntValue(123);
            original.setLongValue(456789L);
            original.setBigDecimalValue(new BigDecimal("123.456"));

            String json = JsonUtils.toJson(original);
            NumberObject converted = JsonUtils.toObject(json, NumberObject.class);

            assertEquals(original.getIntValue(), converted.getIntValue());
            assertEquals(original.getLongValue(), converted.getLongValue());
            assertEquals(original.getBigDecimalValue(), converted.getBigDecimalValue());
        }

        @Test
        @DisplayName("测试 List 双向转换")
        void testListBidirectional() {
            List<SimpleObject> original = List.of(
                    new SimpleObject("张三", 25),
                    new SimpleObject("李四", 30)
            );

            String json = JsonUtils.toJson(original);
            List<SimpleObject> converted = JsonUtils.toObject(json, new TypeReference<List<SimpleObject>>() {});

            assertEquals(original.size(), converted.size());
            assertEquals(original.get(0).getName(), converted.get(0).getName());
            assertEquals(original.get(1).getName(), converted.get(1).getName());
        }
    }

    @Nested
    @DisplayName("特殊情况测试")
    class SpecialCasesTest {

        @Test
        @DisplayName("测试空对象序列化")
        void testEmptyObjectSerialization() {
            SimpleObject obj = new SimpleObject();
            String json = JsonUtils.toJson(obj);

            assertNotNull(json);
            assertTrue(json.contains("\"name\":null"));
            assertTrue(json.contains("\"age\":null"));
        }

        @Test
        @DisplayName("测试嵌套对象序列化")
        void testNestedObjectSerialization() {
            GenericWrapper<SimpleObject> wrapper = new GenericWrapper<>();
            wrapper.setData(new SimpleObject("张三", 25));
            wrapper.setMessage("success");

            String json = JsonUtils.toJson(wrapper);

            assertNotNull(json);
            assertTrue(json.contains("\"data\":{"));
            assertTrue(json.contains("\"name\":\"张三\""));
            assertTrue(json.contains("\"message\":\"success\""));
        }

        @Test
        @DisplayName("测试嵌套对象反序列化")
        void testNestedObjectDeserialization() {
            String json = "{\"data\":{\"name\":\"张三\",\"age\":25},\"message\":\"success\"}";

            GenericWrapper<SimpleObject> wrapper = JsonUtils.toObject(json, GenericWrapper.class, SimpleObject.class);

            assertNotNull(wrapper);
            assertEquals("success", wrapper.getMessage());
        }

        @Test
        @DisplayName("测试中文字符处理")
        void testChineseCharacters() {
            SimpleObject obj = new SimpleObject("中文测试名称", 100);

            String json = JsonUtils.toJson(obj);
            SimpleObject converted = JsonUtils.toObject(json, SimpleObject.class);

            assertEquals("中文测试名称", converted.getName());
        }

        @Test
        @DisplayName("测试特殊字符处理")
        void testSpecialCharacters() {
            SimpleObject obj = new SimpleObject("test\"with\\special/chars", 100);

            String json = JsonUtils.toJson(obj);
            SimpleObject converted = JsonUtils.toObject(json, SimpleObject.class);

            assertEquals("test\"with\\special/chars", converted.getName());
        }

        @Test
        @DisplayName("测试 null 属性值处理")
        void testNullPropertyValue() {
            SimpleObject obj = new SimpleObject(null, null);

            String json = JsonUtils.toJson(obj);
            assertNotNull(json);

            SimpleObject converted = JsonUtils.toObject(json, SimpleObject.class);
            assertNull(converted.getName());
            assertNull(converted.getAge());
        }
    }

    @Nested
    @DisplayName("属性命名策略测试")
    class PropertyNamingStrategyTest {

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        static class CamelCaseObject {
            private String firstName;
            private String lastName;
            private Integer userAge;
        }

        @Test
        @DisplayName("测试小驼峰命名策略")
        void testLowerCamelCaseNaming() {
            CamelCaseObject obj = new CamelCaseObject("张", "三", 25);

            String json = JsonUtils.toJson(obj);

            assertTrue(json.contains("\"firstName\":"));
            assertTrue(json.contains("\"lastName\":"));
            assertTrue(json.contains("\"userAge\":"));
        }
    }

}
