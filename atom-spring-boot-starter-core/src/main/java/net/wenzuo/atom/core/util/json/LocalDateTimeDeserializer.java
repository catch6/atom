package net.wenzuo.atom.core.util.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Catch
 * @since 2025-06-13
 */
@JacksonStdImpl
public class LocalDateTimeDeserializer extends com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer {

    public LocalDateTimeDeserializer() {
        super();
    }

    public LocalDateTimeDeserializer(DateTimeFormatter formatter) {
        super(formatter);
    }

    public LocalDateTimeDeserializer(com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer base, Boolean leniency) {
        super(base, leniency);
    }

    public LocalDateTimeDeserializer(com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer base, Boolean leniency, DateTimeFormatter formatter, JsonFormat.Shape shape, Boolean readTimestampsAsNanosOverride) {
        super(base, leniency, formatter, shape, readTimestampsAsNanosOverride);
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasTokenId(6)) {
            return this._fromString(parser, context, parser.getText());
        } else if (parser.isExpectedStartObjectToken()) {
            return this._fromString(parser, context, context.extractScalarFromObject(parser, this, this.handledType()));
        } else {
            if (parser.isExpectedStartArrayToken()) {
                JsonToken t = parser.nextToken();
                if (t == JsonToken.END_ARRAY) {
                    return null;
                }

                if ((t == JsonToken.VALUE_STRING || t == JsonToken.VALUE_EMBEDDED_OBJECT) && context.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                    LocalDateTime parsed = this.deserialize(parser, context);
                    if (parser.nextToken() != JsonToken.END_ARRAY) {
                        this.handleMissingEndArrayForSingle(parser, context);
                    }

                    return parsed;
                }

                if (t == JsonToken.VALUE_NUMBER_INT) {
                    int year = parser.getIntValue();
                    int month = parser.nextIntValue(-1);
                    int day = parser.nextIntValue(-1);
                    int hour = parser.nextIntValue(-1);
                    int minute = parser.nextIntValue(-1);
                    t = parser.nextToken();
                    LocalDateTime result;
                    if (t == JsonToken.END_ARRAY) {
                        result = LocalDateTime.of(year, month, day, hour, minute);
                    } else {
                        int second = parser.getIntValue();
                        t = parser.nextToken();
                        if (t == JsonToken.END_ARRAY) {
                            result = LocalDateTime.of(year, month, day, hour, minute, second);
                        } else {
                            int partialSecond = parser.getIntValue();
                            if (partialSecond < 1000 && !this.shouldReadTimestampsAsNanoseconds(context)) {
                                partialSecond *= 1000000;
                            }

                            if (parser.nextToken() != JsonToken.END_ARRAY) {
                                throw context.wrongTokenException(parser, this.handledType(), JsonToken.END_ARRAY, "Expected array to end");
                            }

                            result = LocalDateTime.of(year, month, day, hour, minute, second, partialSecond);
                        }
                    }

                    return result;
                }

                context.reportInputMismatch(this.handledType(), "Unexpected token (%s) within Array, expected VALUE_NUMBER_INT", new Object[]{t});
            }

            if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
                return (LocalDateTime) parser.getEmbeddedObject();
            } else {
                if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                    int length = parser.getTextLength();
                    Instant instant;
                    if (length > 10) {
                        instant = Instant.ofEpochMilli(parser.getLongValue());
                    } else {
                        instant = Instant.ofEpochSecond(parser.getLongValue());
                    }
                    return LocalDateTime.ofInstant(instant, context.getTimeZone().toZoneId());
                }

                return (LocalDateTime) this._handleUnexpectedToken(context, parser, "Expected array or string.", new Object[0]);
            }
        }
    }

}
