/*
 * Copyright (c) 2022-2023 Catch (catchlife6@163.com)
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.jwt.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import lombok.RequiredArgsConstructor;
import net.wenzuo.atom.jwt.properties.JwtProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Base64;

/**
 * @author Catch
 * @since 2023-06-06
 */
@RequiredArgsConstructor
@ComponentScan("net.wenzuo.atom.jwt")
@ConfigurationPropertiesScan("net.wenzuo.atom.jwt.properties")
@ConditionalOnProperty(value = "atom.jwt.enabled", matchIfMissing = true)
public class JwtAutoConfiguration {

    private final JwtProperties jwtProperties;

    @ConditionalOnMissingBean
    @Bean
    public JWSSigner jwsSigner() {
        String secret = jwtProperties.getSecret();
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("jwt secret can not be empty");
        }
        byte[] secretKey = Base64.getDecoder()
                                 .decode(secret);
        try {
            return new MACSigner(secretKey);
        } catch (KeyLengthException e) {
            throw new RuntimeException(e);
        }
    }

    @ConditionalOnMissingBean
    @Bean
    public JWSVerifier jwsVerifier() {
        String secret = jwtProperties.getSecret();
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("jwt secret can not be empty");
        }
        byte[] secretKey = Base64.getDecoder()
                                 .decode(secret);
        try {
            return new MACVerifier(secretKey);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

}
