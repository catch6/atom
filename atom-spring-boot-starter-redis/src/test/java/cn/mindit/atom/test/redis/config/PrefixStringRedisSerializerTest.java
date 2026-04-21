package cn.mindit.atom.test.redis.config;

import cn.mindit.atom.redis.config.PrefixStringRedisSerializer;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class PrefixStringRedisSerializerTest {

    @Test
    void serializeWithoutPrefix() {
        PrefixStringRedisSerializer serializer = new PrefixStringRedisSerializer();
        byte[] result = serializer.serialize("mykey");
        assertThat(new String(result, StandardCharsets.UTF_8)).isEqualTo("mykey");
    }

    @Test
    void serializeWithPrefix() {
        PrefixStringRedisSerializer serializer = new PrefixStringRedisSerializer("app:");
        byte[] result = serializer.serialize("mykey");
        assertThat(new String(result, StandardCharsets.UTF_8)).isEqualTo("app:mykey");
    }

    @Test
    void deserializeWithPrefix() {
        PrefixStringRedisSerializer serializer = new PrefixStringRedisSerializer("app:");
        byte[] bytes = "app:mykey".getBytes(StandardCharsets.UTF_8);
        String result = serializer.deserialize(bytes);
        assertThat(result).isEqualTo("mykey");
    }

    @Test
    void deserializeWithoutMatchingPrefix() {
        PrefixStringRedisSerializer serializer = new PrefixStringRedisSerializer("app:");
        byte[] bytes = "other:mykey".getBytes(StandardCharsets.UTF_8);
        String result = serializer.deserialize(bytes);
        assertThat(result).isEqualTo("other:mykey");
    }

    @Test
    void deserializeWithEmptyPrefix() {
        PrefixStringRedisSerializer serializer = new PrefixStringRedisSerializer("");
        byte[] bytes = "mykey".getBytes(StandardCharsets.UTF_8);
        String result = serializer.deserialize(bytes);
        assertThat(result).isEqualTo("mykey");
    }

    @Test
    void roundTripWithPrefix() {
        PrefixStringRedisSerializer serializer = new PrefixStringRedisSerializer("prefix:");
        byte[] serialized = serializer.serialize("testkey");
        String deserialized = serializer.deserialize(serialized);
        assertThat(deserialized).isEqualTo("testkey");
    }

    @Test
    void roundTripWithEmptyPrefix() {
        PrefixStringRedisSerializer serializer = new PrefixStringRedisSerializer();
        byte[] serialized = serializer.serialize("testkey");
        String deserialized = serializer.deserialize(serialized);
        assertThat(deserialized).isEqualTo("testkey");
    }

    @Test
    void serializeWithCharsetAndPrefix() {
        PrefixStringRedisSerializer serializer = new PrefixStringRedisSerializer(StandardCharsets.UTF_8, "ns:");
        byte[] result = serializer.serialize("key");
        assertThat(new String(result, StandardCharsets.UTF_8)).isEqualTo("ns:key");
    }

    @Test
    void constructorWithCharsetOnly() {
        PrefixStringRedisSerializer serializer = new PrefixStringRedisSerializer(StandardCharsets.UTF_8);
        byte[] result = serializer.serialize("key");
        assertThat(new String(result, StandardCharsets.UTF_8)).isEqualTo("key");
    }
}
