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

package cn.mindit.atom.core.util;

import cn.hutool.core.io.IoUtil;
import cn.mindit.atom.core.util.json.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalTimeSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Json 处理工具类
 *
 * @author Catch
 * @since 2021-06-29
 */
@Slf4j
public class JsonUtils {

    private JsonUtils() {
    }

    public static JsonMapper jsonMapper = jsonMapper();

    /**
     * 将 Java 对象转为 Json 字符串
     *
     * @param <T>    泛型
     * @param object Java 对象
     * @return json 字符串
     */
    public static <T> String toJson(T object) {
        return switch (object) {
            case null -> null;
            case CharSequence charSequence -> charSequence.toString();
            case Number number -> number.toString();
            default -> jsonMapper.writeValueAsString(object);
        };
    }

    /**
     * 将 Java 对象转为格式化的 Json 字符串
     *
     * @param <T>    泛型
     * @param object Java 对象
     * @return json 字符串
     */
    public static <T> String toPrettyJson(T object) {
        return switch (object) {
            case null -> null;
            case CharSequence charSequence -> charSequence.toString();
            case Number number -> number.toString();
            default -> jsonMapper.writerWithDefaultPrettyPrinter()
                                 .writeValueAsString(object);
        };
    }

    /**
     * 将 Json 字符串转为 Object 对象
     *
     * @param <T>   泛型
     * @param json  json 字符串
     * @param clazz 要转换的 java 类型
     * @return 接收 java 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(String json, Class<T> clazz) {
        if (CharSequence.class.isAssignableFrom(clazz)) {
            return (T) json;
        }
        return (json == null || json.isEmpty()) ? null : jsonMapper.readValue(json, clazz);
    }

    /**
     * 将 Json 字符串输入流转为 Object 对象
     *
     * @param <T>         泛型
     * @param inputStream json 字符串输入流
     * @param clazz       要转换的 java 类型
     * @return 接收 java 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(InputStream inputStream, Class<T> clazz) {
        if (CharSequence.class.isAssignableFrom(clazz)) {
            return (T) IoUtil.readUtf8(inputStream);
        }
        return (inputStream == null) ? null : jsonMapper.readValue(inputStream, clazz);
    }

    /**
     * 将 Json 字符串转为 Object 对象
     *
     * @param json    json 字符串
     * @param wrapper 泛型包装类
     * @param inners  泛型类
     * @param <T>     泛型包装类
     * @return 泛型包装类
     */
    public static <T> T toObject(String json, Class<?> wrapper, Class<?>... inners) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        JavaType javaType = jsonMapper.getTypeFactory()
                                      .constructParametricType(wrapper, inners);
        return jsonMapper.readValue(json, javaType);
    }

    /**
     * 将 Json 字符串输入流转为 Object 对象
     *
     * @param inputStream json 字符串输入流
     * @param wrapper     泛型包装类
     * @param inners      泛型类
     * @param <T>         泛型包装类
     * @return 泛型包装类
     */
    public static <T> T toObject(InputStream inputStream, Class<?> wrapper, Class<?>... inners) {
        if (inputStream == null) {
            return null;
        }
        JavaType javaType = jsonMapper.getTypeFactory()
                                      .constructParametricType(wrapper, inners);
        return jsonMapper.readValue(inputStream, javaType);
    }

    /**
     * 将 Json 字符串转为 Object 对象
     *
     * @param <T>  泛型
     * @param json json 字符串
     * @param type 要转换的 java 类型
     * @return 接收 java 对象
     */
    public static <T> T toObject(String json, Type type) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        JavaType javaType = jsonMapper.getTypeFactory()
                                      .constructType(type);
        return jsonMapper.readValue(json, javaType);
    }

    /**
     * 将 Json 字符串输入流转为 Object 对象
     *
     * @param <T>         泛型
     * @param inputStream json 字符串输入流
     * @param type        要转换的 java 类型
     * @return 接收 java 对象
     */
    public static <T> T toObject(InputStream inputStream, Type type) {
        if (inputStream == null) {
            return null;
        }
        JavaType javaType = jsonMapper.getTypeFactory()
                                      .constructType(type);
        return jsonMapper.readValue(inputStream, javaType);
    }

    /**
     * 将 Json 字符串转为 Object 对象
     *
     * @param <T>  泛型
     * @param json json 字符串
     * @param type 要转换的 java 类型
     * @return 接收 java 对象
     */
    public static <T> T toObject(String json, TypeReference<T> type) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return jsonMapper.readValue(json, type);
    }

    /**
     * 将 Json 字符串输入流转为 Object 对象
     *
     * @param <T>         泛型
     * @param inputStream json 字符串输入流
     * @param type        要转换的 java 类型
     * @return 接收 java 对象
     */
    public static <T> T toObject(InputStream inputStream, TypeReference<T> type) {
        if (inputStream == null) {
            return null;
        }
        return jsonMapper.readValue(inputStream, type);
    }

    /**
     * 获取 jsonMapper
     *
     * @return jsonMapper
     */
    public static JsonMapper jsonMapper() {
        JsonMapper.Builder builder = JsonMapper.builder();
        customize().customize(builder);
        return builder.build();
    }

    public static JsonMapperBuilderCustomizer customize() {
        // ==================== 日期时间的处理 ====================
        String dateFormat = "yyyy-MM-dd";
        String timeFormat = "HH:mm:ss";
        String datetimeFormat = "yyyy-MM-dd HH:mm:ss";

        // JDK util 包下的 Date java.util.date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimeFormat);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timeFormat);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datetimeFormat);

        LocalDateSerializer localDateSerializer = new LocalDateSerializer(dateFormatter);
        LocalDateDeserializer localDateDeserializer = new LocalDateDeserializer(dateFormatter);

        LocalTimeSerializer localTimeSerializer = new LocalTimeSerializer(timeFormatter);
        LocalTimeDeserializer localTimeDeserializer = new LocalTimeDeserializer(timeFormatter);

        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(dateTimeFormatter);
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(dateTimeFormatter);

        // 解决Long精度丢失，Long to String
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(BigDecimal.class, BigDecimalSerializer.instance);

        simpleModule.addDeserializer(Integer.class, IntegerDeserializer.instance);
        simpleModule.addDeserializer(Integer.TYPE, IntegerDeserializer.instance);
        simpleModule.addDeserializer(Long.class, LongDeserializer.instance);
        simpleModule.addDeserializer(Long.TYPE, LongDeserializer.instance);
        simpleModule.addDeserializer(BigDecimal.class, BigDecimalDeserializer.instance);

        simpleModule.addSerializer(LocalDate.class, localDateSerializer)
                    .addDeserializer(LocalDate.class, localDateDeserializer)
                    .addSerializer(LocalTime.class, localTimeSerializer)
                    .addDeserializer(LocalTime.class, localTimeDeserializer)
                    .addSerializer(LocalDateTime.class, localDateTimeSerializer)
                    .addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

        return builder -> builder.defaultLocale(Locale.SIMPLIFIED_CHINESE)
                                 .defaultTimeZone(TimeZone.getDefault())
                                 .defaultDateFormat(simpleDateFormat)
                                 // 属性名策略: 小驼峰
                                 .propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
                                 // 若对象的属性值为null，序列化时不显示
                                 .changeDefaultPropertyInclusion(include -> include.withValueInclusion(JsonInclude.Include.ALWAYS))
                                 .changeDefaultPropertyInclusion(include -> include.withContentInclusion(JsonInclude.Include.ALWAYS))
                                 // 即如果一个类没有public的方法或属性时，会导致序列化失败。关闭后，会得到一个空JSON串。
                                 .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                                 .addModules(simpleModule);
    }

}
