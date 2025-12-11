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

/**
 * @author Catch
 * @since 2024-12-20
 */
@JacksonStdImpl
public class LongDeserializer extends ValueDeserializer<Long> {

    public static final LongDeserializer instance = new LongDeserializer();

    @Override
    public Long deserialize(JsonParser parser, DeserializationContext context) {
        String value = parser.getString();
        if (value == null || value.isEmpty()) {
            return null;
        }
        JsonToken token = parser.currentToken();
        if (token.isBoolean()) {
            return parser.getBooleanValue() ? 1L : 0L;
        }
        value = value.trim();
        return Long.parseLong(value);
    }

}
