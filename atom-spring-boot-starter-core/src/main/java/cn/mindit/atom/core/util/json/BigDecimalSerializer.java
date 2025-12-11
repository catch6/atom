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

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonToken;
import tools.jackson.core.type.WritableTypeId;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.annotation.JacksonStdImpl;
import tools.jackson.databind.jsontype.TypeSerializer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author Catch
 * @since 2024-12-20
 */
@JacksonStdImpl
public class BigDecimalSerializer extends ValueSerializer<BigDecimal> {

    public static final BigDecimalSerializer instance = new BigDecimalSerializer();

    private DecimalFormat decimalFormat;

    public BigDecimalSerializer() {
    }

    public BigDecimalSerializer(String pattern, RoundingMode roundingMode) {
        this.decimalFormat = new DecimalFormat(pattern);
        decimalFormat.setRoundingMode(roundingMode);
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializationContext context) {
        if (decimalFormat == null) {
            gen.writeString(value.toPlainString());
            return;
        }
        gen.writeString(decimalFormat.format(value));
    }

    @Override
    public void serializeWithType(BigDecimal value, JsonGenerator gen, SerializationContext context, TypeSerializer typeSer) {
        WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen, context, typeSer.typeId(value, JsonToken.VALUE_STRING));
        serialize(value, gen, context);
        typeSer.writeTypeSuffix(gen, context, typeIdDef);
    }

    @Override
    public ValueSerializer<?> createContextual(SerializationContext context, BeanProperty property) {
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
