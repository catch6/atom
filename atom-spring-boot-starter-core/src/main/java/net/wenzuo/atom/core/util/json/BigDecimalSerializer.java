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

package net.wenzuo.atom.core.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author Catch
 * @since 2024-12-20
 */
@JacksonStdImpl
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> implements ContextualSerializer {

    public static final BigDecimalSerializer instance = new BigDecimalSerializer();

    private DecimalFormat decimalFormat;

    public BigDecimalSerializer() {
    }

    public BigDecimalSerializer(String pattern, RoundingMode roundingMode) {
        this.decimalFormat = new DecimalFormat(pattern);
        decimalFormat.setRoundingMode(roundingMode);
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (decimalFormat == null) {
            gen.writeString(value.toPlainString());
            return;
        }
        gen.writeString(decimalFormat.format(value));
    }

    @Override
    public void serializeWithType(BigDecimal value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen, typeSer.typeId(value, JsonToken.VALUE_STRING));
        serialize(value, gen, serializers);
        typeSer.writeTypeSuffix(gen, typeIdDef);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        if (property == null) {
            return BigDecimalSerializer.instance;
        }
        JsonDecimalFormat annotation = property.getAnnotation(JsonDecimalFormat.class);
        if (annotation == null) {
            annotation = property.getContextAnnotation(JsonDecimalFormat.class);
        }
        if (annotation == null) {
            return BigDecimalSerializer.instance;
        }
        return new BigDecimalSerializer(annotation.value(), annotation.roundingMode());
    }

}
