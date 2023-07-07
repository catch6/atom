/*
 * Copyright (c) 2022-2023 Catch
 * [Atom] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
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
public abstract class JsonUtils {

	private static PropertyNamingStrategy propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE;
	public static ObjectMapper objectMapper = objectMapper();

	/**
	 * 更改属性序列化和反序列化命名策略, 默认为 LOWER_CAMEL_CASE
	 * <p>
	 * 如果不使用默认命名策略,需要在项目启动前就设置命名策略.
	 * 如: JsonUtils.setDefaultPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
	 */
	public static void setDefaultPropertyNamingStrategy(PropertyNamingStrategy strategy) {
		propertyNamingStrategy = strategy;
		objectMapper = objectMapper();
	}

	/**
	 * 将 Java 对象转为 Json 字符串
	 *
	 * @param <T>    泛型
	 * @param object Java 对象
	 * @return json 字符串
	 */
	public static <T> String toJson(T object) {
		if (object == null) {
			return null;
		}
		if (object instanceof String) {
			return (String) object;
		}
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将 Java 对象转为格式化的 Json 字符串
	 *
	 * @param <T>    泛型
	 * @param object Java 对象
	 * @return json 字符串
	 */
	public static <T> String toPrettyJson(T object) {
		if (object == null) {
			return null;
		}
		if (object instanceof String) {
			return (String) object;
		}
		try {
			return objectMapper.writerWithDefaultPrettyPrinter()
							   .writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将 Json 字符串转为 Object 对象
	 *
	 * @param <T>   泛型
	 * @param json  json 字符串
	 * @param clazz 要转换的 java 类型
	 * @return 接收 java 对象
	 */
	public static <T> T toObject(String json, Class<T> clazz) {
		try {
			return (json == null || json.isEmpty()) ? null : objectMapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将 Json 字符串输入流转为 Object 对象
	 *
	 * @param <T>         泛型
	 * @param inputStream json 字符串输入流
	 * @param clazz       要转换的 java 类型
	 * @return 接收 java 对象
	 */
	public static <T> T toObject(InputStream inputStream, Class<T> clazz) {
		try {
			return (inputStream == null) ? null : objectMapper.readValue(inputStream, clazz);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
		JavaType javaType = objectMapper.getTypeFactory()
										.constructParametricType(wrapper, inners);
		try {
			return objectMapper.readValue(json, javaType);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
		JavaType javaType = objectMapper.getTypeFactory()
										.constructParametricType(wrapper, inners);
		try {
			return objectMapper.readValue(inputStream, javaType);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
		JavaType javaType = objectMapper.getTypeFactory()
										.constructType(type);
		try {
			return objectMapper.readValue(json, javaType);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
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
		JavaType javaType = objectMapper.getTypeFactory()
										.constructType(type);
		try {
			return objectMapper.readValue(inputStream, javaType);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
		try {
			return objectMapper.readValue(json, type);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
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
		try {
			return objectMapper.readValue(inputStream, type);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取 ObjectMapper
	 *
	 * @return ObjectMapper
	 */
	public static ObjectMapper objectMapper() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		customize().customize(builder);
		ObjectMapper objectMapper = new ObjectMapper();
		builder.configure(objectMapper);
		return objectMapper;
	}

	public static Jackson2ObjectMapperBuilderCustomizer customize() {
		// ==================== 日期时间的处理 ====================
		String dateFormat = "yyyy-MM-dd";
		String timeFormat = "HH:mm:ss";
		String datetimeFormat = "yyyy-MM-dd HH:mm:ss";
		String timeZone = "GMT+8";

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
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDate.class, localDateSerializer)
					  .addDeserializer(LocalDate.class, localDateDeserializer)
					  .addSerializer(LocalTime.class, localTimeSerializer)
					  .addDeserializer(LocalTime.class, localTimeDeserializer)
					  .addSerializer(LocalDateTime.class, localDateTimeSerializer)
					  .addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

		Jdk8Module jdk8Module = new Jdk8Module();

		ParameterNamesModule parameterNamesModule = new ParameterNamesModule();

		return builder -> builder.locale(Locale.SIMPLIFIED_CHINESE)
								 .timeZone(TimeZone.getTimeZone(timeZone))
								 .dateFormat(simpleDateFormat)
								 // 属性名策略: 小驼峰
								 .propertyNamingStrategy(propertyNamingStrategy)
								 // 若对象的属性值为null，序列化时不显示
								 .serializationInclusion(JsonInclude.Include.ALWAYS)
								 .featuresToEnable()
								 .featuresToDisable(
									 // 即如果一个类没有public的方法或属性时，会导致序列化失败。关闭后，会得到一个空JSON串。
									 SerializationFeature.FAIL_ON_EMPTY_BEANS,
									 // 默认开启,即将Date类型序列化为数字时间戳(毫秒表示)。关闭后，按格式化的时间输出,见下面的 datetimeFormat
									 SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
									 // 若POJO中不含有JSON中的属性，则抛出异常。关闭后，不解析该字段，而不会抛出异常
									 DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
								 .modules(simpleModule, javaTimeModule, jdk8Module, parameterNamesModule);
	}

}
