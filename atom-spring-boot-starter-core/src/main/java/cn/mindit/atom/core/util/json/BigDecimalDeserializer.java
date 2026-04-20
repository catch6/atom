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
