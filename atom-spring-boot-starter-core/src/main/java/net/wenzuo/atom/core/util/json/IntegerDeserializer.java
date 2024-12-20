/*
 * Copyright (c) 2022-2024 Catch(catchlife6@163.com).
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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import java.io.IOException;

/**
 * @author Catch
 * @since 2024-12-20
 */
@JacksonStdImpl
public class IntegerDeserializer extends JsonDeserializer<Integer> {

    public static final IntegerDeserializer instance = new IntegerDeserializer();

    @Override
    public Integer deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        // 只处理布尔类型
        JsonToken token = parser.getCurrentToken();
        if (token.isBoolean()) {
            return parser.getBooleanValue() ? 1 : 0;
        }
        return parser.getValueAsInt();
    }

}
