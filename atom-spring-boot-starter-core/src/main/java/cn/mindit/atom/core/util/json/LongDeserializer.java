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
