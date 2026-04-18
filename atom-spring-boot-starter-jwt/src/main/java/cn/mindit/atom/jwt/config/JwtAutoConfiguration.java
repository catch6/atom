/*
 * Copyright (c) 2022-2026 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.jwt.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import lombok.RequiredArgsConstructor;
import cn.mindit.atom.jwt.service.impl.JwtServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Base64;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Import(JwtServiceImpl.class)
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnProperty(value = "atom.jwt.enabled", matchIfMissing = true)
@AutoConfiguration
public class JwtAutoConfiguration {

    private final JwtProperties jwtProperties;

    @ConditionalOnMissingBean
    @Bean
    public JWSSigner jwsSigner() {
        byte[] secretKey = decodeSecret(jwtProperties.getSecret());
        try {
            return new MACSigner(secretKey);
        } catch (KeyLengthException e) {
            throw new IllegalArgumentException("jwt secret key length is insufficient for MAC signer: " + e.getMessage(), e);
        }
    }

    @ConditionalOnMissingBean
    @Bean
    public JWSVerifier jwsVerifier() {
        byte[] secretKey = decodeSecret(jwtProperties.getSecret());
        try {
            return new MACVerifier(secretKey);
        } catch (JOSEException e) {
            throw new IllegalArgumentException("jwt secret key length is insufficient for MAC verifier: " + e.getMessage(), e);
        }
    }

    private static byte[] decodeSecret(String secret) {
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("jwt secret can not be empty");
        }
        try {
            return Base64.getDecoder().decode(secret);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("jwt secret must be Base64 encoded", e);
        }
    }

}
