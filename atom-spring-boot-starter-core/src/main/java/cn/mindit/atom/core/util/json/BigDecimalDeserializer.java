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

import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.annotation.JacksonStdImpl;

import java.math.BigDecimal;

/**
 * @author Catch
 * @since 2024-12-20
 */
@JacksonStdImpl
public class BigDecimalDeserializer extends ValueDeserializer<BigDecimal> {

    public static final BigDecimalDeserializer instance = new BigDecimalDeserializer();

    @Override
    public BigDecimal deserialize(JsonParser parser, DeserializationContext ctx) {
        String value = parser.getString();
        if (value == null || value.isEmpty()) {
            return null;
        }
        JsonToken token = parser.currentToken();
        if (token.isBoolean()) {
            return parser.getBooleanValue() ? BigDecimal.ONE : BigDecimal.ZERO;
        }
        value = value.trim();
        return new BigDecimal(value);
    }

}
