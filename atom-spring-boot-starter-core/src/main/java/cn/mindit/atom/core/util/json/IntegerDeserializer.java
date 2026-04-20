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
public class IntegerDeserializer extends ValueDeserializer<Integer> {

    public static final IntegerDeserializer instance = new IntegerDeserializer();

    @Override
    public Integer deserialize(JsonParser parser, DeserializationContext ctx) {
        String value = parser.getString();
        if (value == null || value.isEmpty()) {
            return null;
        }
        JsonToken token = parser.currentToken();
        if (token.isBoolean()) {
            return parser.getBooleanValue() ? 1 : 0;
        }
        value = value.trim();
        return Integer.parseInt(value);
    }

}
