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

package net.wenzuo.atom.redis.config;

import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;

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
