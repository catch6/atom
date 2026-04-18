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

package cn.mindit.atom.test.jwt.service;

import cn.mindit.atom.jwt.service.JwtService;
import cn.mindit.atom.jwt.service.impl.JwtServiceImpl;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceImplTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() throws Exception {
        byte[] secret = new byte[32];
        new SecureRandom().nextBytes(secret);
        jwtService = new JwtServiceImpl(new MACSigner(secret), new MACVerifier(secret));
    }

    @Test
    void sign_returnsNonEmptyToken() {
        String token = jwtService.sign(new Payload("user1", 25));
        assertThat(token).isNotBlank();
    }

    @Test
    void sign_withStringPayload_returnsToken() {
        String token = jwtService.sign("hello");
        assertThat(token).isNotBlank();
    }

    @Test
    void parse_validToken_returnsOriginalPayload() {
        Payload original = new Payload("user1", 25);
        String token = jwtService.sign(original);

        Payload result = jwtService.parse(token, Payload.class);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("user1");
        assertThat(result.getAge()).isEqualTo(25);
    }

    @Test
    void parse_withStringClass_returnsPayloadString() {
        String token = jwtService.sign("hello");

        String result = jwtService.parse(token, String.class);

        assertThat(result).isEqualTo("hello");
    }

    @Test
    void parse_objectPayloadAsString_returnsJsonString() {
        Payload original = new Payload("admin", 30);
        String token = jwtService.sign(original);

        String result = jwtService.parse(token, String.class);

        assertThat(result).contains("admin").contains("30");
    }

    @Test
    void parse_withDifferentKey_returnsNull() throws Exception {
        String token = jwtService.sign(new Payload("user1", 25));

        byte[] otherSecret = new byte[32];
        new SecureRandom().nextBytes(otherSecret);
        JwtService otherService = new JwtServiceImpl(new MACSigner(otherSecret), new MACVerifier(otherSecret));

        assertThat(otherService.parse(token, Payload.class)).isNull();
    }

    @Test
    void parse_malformedToken_returnsNull() {
        assertThat(jwtService.parse("not.a.valid-jwt", Payload.class)).isNull();
    }

    @Test
    void parse_tamperedToken_returnsNull() {
        String token = jwtService.sign(new Payload("user1", 25));
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";

        assertThat(jwtService.parse(tampered, Payload.class)).isNull();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload {
        private String username;
        private Integer age;
    }

}
