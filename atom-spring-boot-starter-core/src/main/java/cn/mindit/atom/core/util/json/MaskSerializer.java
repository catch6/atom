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

package cn.mindit.atom.core.util.json;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.mindit.atom.core.util.MaskType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

/**
 * @author Catch
 * @since 2023-08-25
 */
public class MaskSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    public static final MaskSerializer instance = new MaskSerializer();

    private MaskType type;

    private Integer start;

    private Integer end;

    public MaskSerializer() {
    }

    public MaskSerializer(MaskType type, Integer start, Integer end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        if (valueStr.isEmpty()) {
            gen.writeString("");
            return;
        }
        switch (type) {
            // 自定义类型脱敏
            case CUSTOM -> gen.writeString(StrUtil.hide(valueStr, start, end));

            // 中文姓名脱敏
            case NAME -> gen.writeString(StrUtil.hide(valueStr, 1, valueStr.length()));

            // 手机号脱敏
            case PHONE -> gen.writeString(StrUtil.hide(valueStr, 3, valueStr.length() - 4));

            // 身份证脱敏
            case ID_CARD -> gen.writeString(StrUtil.hide(valueStr, 3, valueStr.length() - 4));

            // 银行卡脱敏
            case BANK_CARD -> gen.writeString("**** **** **** " + valueStr.substring(valueStr.length() - 4));

            // 邮箱脱敏
            case EMAIL -> gen.writeString(DesensitizedUtil.email(value.toString()));

            // 密码脱敏
            case PASSWORD -> gen.writeString(DesensitizedUtil.password(value.toString()));

            // 固定电话脱敏
            case FIXED_PHONE -> gen.writeString(DesensitizedUtil.fixedPhone(value.toString()));

            // 地址脱敏
            case ADDRESS -> gen.writeString(DesensitizedUtil.address(value.toString(), 6));

            // ID 脱敏
            case ID -> {
                if (value instanceof Number) {
                    gen.writeNumber(0);
                } else {
                    gen.writeString("0");
                }
            }

            // 中国车牌脱敏
            case CAR_LICENSE -> gen.writeString(DesensitizedUtil.carLicense(value.toString()));

            // IPv4
            case IPV4 -> gen.writeString(DesensitizedUtil.ipv4(value.toString()));

            // IPv6
            case IPV6 -> gen.writeString(DesensitizedUtil.ipv6(value.toString()));

            default -> {
            }
        }
    }

    @Override
    public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen, typeSer.typeId(value, JsonToken.VALUE_STRING));
        serialize(value, gen, serializers);
        typeSer.writeTypeSuffix(gen, typeIdDef);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        if (property == null) {
            return MaskSerializer.instance;
        }
        // 获取定义的注解
        JsonMask annotation = property.getAnnotation(JsonMask.class);
        if (annotation == null) {
            annotation = property.getContextAnnotation(JsonMask.class);
        }
        if (annotation != null) {
            return new MaskSerializer(annotation.value(), annotation.start(), annotation.end());
        }
        return provider.findValueSerializer(property.getType(), property);
    }

}
