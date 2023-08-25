/*
 * Copyright (c) 2022-2023 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.core.util;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * @author Catch
 * @since 2023-08-25
 */
@AllArgsConstructor
@NoArgsConstructor
public class DesensitizationSerialize extends JsonSerializer<Object> implements ContextualSerializer {

	private DesensitizationType type;

	private Integer start;

	private Integer end;

	@Override
	public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		if (value == null) {
			return;
		}
		switch (type) {
			// 自定义类型脱敏
			case CUSTOM:
				jsonGenerator.writeString(CharSequenceUtil.hide(value.toString(), start, end));
				return;

			// userId脱敏
			case USER_ID:
				if (value instanceof Number) {
					jsonGenerator.writeNumber(String.valueOf(DesensitizedUtil.userId()));
				} else {
					jsonGenerator.writeString(String.valueOf(DesensitizedUtil.userId()));
				}
				return;

			// 中文姓名脱敏
			case CHINESE_NAME:
				jsonGenerator.writeString(DesensitizedUtil.chineseName(value.toString()));
				return;

			// 身份证脱敏
			case ID_CARD:
				jsonGenerator.writeString(DesensitizedUtil.idCardNum(value.toString(), 1, 2));
				return;

			// 手机号脱敏
			case MOBILE_PHONE:
				jsonGenerator.writeString(DesensitizedUtil.mobilePhone(value.toString()));
				return;

			// 固定电话脱敏
			case FIXED_PHONE:
				jsonGenerator.writeString(DesensitizedUtil.fixedPhone(value.toString()));
				return;

			// 地址脱敏
			case ADDRESS:
				jsonGenerator.writeString(DesensitizedUtil.address(value.toString(), 8));
				return;

			// 邮箱脱敏
			case EMAIL:
				jsonGenerator.writeString(DesensitizedUtil.email(value.toString()));
				return;

			// 密码脱敏
			case PASSWORD:
				jsonGenerator.writeString(DesensitizedUtil.password(value.toString()));
				return;

			// 银行卡脱敏
			case BANK_CARD:
				jsonGenerator.writeString(DesensitizedUtil.bankCard(value.toString()));
				return;

			// 中国车牌脱敏
			case CAR_LICENSE:
				jsonGenerator.writeString(DesensitizedUtil.carLicense(value.toString()));
				return;

			default:
		}
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
		if (beanProperty == null) {
			return serializerProvider.findNullValueSerializer(null);
		}
		// 获取定义的注解
		Desensitization desensitization = beanProperty.getAnnotation(Desensitization.class);
		if (desensitization == null) {
			desensitization = beanProperty.getContextAnnotation(Desensitization.class);
		}
		if (desensitization != null) {
			return new DesensitizationSerialize(desensitization.type(), desensitization.start(), desensitization.end());
		}
		return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
	}

}
