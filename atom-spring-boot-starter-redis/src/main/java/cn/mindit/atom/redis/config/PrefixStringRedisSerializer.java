package cn.mindit.atom.redis.config;

import org.jspecify.annotations.NonNull;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.Charset;

/**
 * @author Catch
 * @since 2024-10-29
 */
public class PrefixStringRedisSerializer extends StringRedisSerializer {

    private final String prefix;

    public PrefixStringRedisSerializer() {
        super();
        this.prefix = "";
    }

    public PrefixStringRedisSerializer(String prefix) {
        super();
        this.prefix = prefix;
    }

    public PrefixStringRedisSerializer(Charset charset) {
        super(charset);
        this.prefix = "";
    }

    public PrefixStringRedisSerializer(Charset charset, String prefix) {
        super(charset);
        this.prefix = prefix;
    }

    @NonNull
    @Override
    public byte[] serialize(String value) throws SerializationException {
        return super.serialize(prefix + value);
    }

    @NonNull
    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        String deserialized = super.deserialize(bytes);
        if (deserialized.startsWith(prefix)) {
            return deserialized.substring(prefix.length());
        }
        return deserialized;
    }

}
