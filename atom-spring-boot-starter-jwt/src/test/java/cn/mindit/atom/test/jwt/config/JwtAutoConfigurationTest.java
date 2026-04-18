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

package cn.mindit.atom.test.jwt.config;

import cn.mindit.atom.jwt.config.JwtAutoConfiguration;
import cn.mindit.atom.jwt.config.JwtProperties;
import cn.mindit.atom.jwt.service.JwtService;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class JwtAutoConfigurationTest {

    private static final String TEST_SECRET = "4cMcaaEjSwcv43JjS93FZhTfNMez8QFAwcDbO76RtBo=";

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(JwtAutoConfiguration.class))
            .withPropertyValues("atom.jwt.secret=" + TEST_SECRET);

    @Test
    void autoConfigurationLoads_whenPropertyNotSet() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(JwtAutoConfiguration.class);
            assertThat(context).hasSingleBean(JwtProperties.class);
        });
    }

    @Test
    void autoConfigurationLoads_whenExplicitlyEnabled() {
        contextRunner
                .withPropertyValues("atom.jwt.enabled=true")
                .run(context -> {
                    assertThat(context).hasSingleBean(JwtAutoConfiguration.class);
                    assertThat(context).hasSingleBean(JwtProperties.class);
                });
    }

    @Test
    void autoConfigurationDisabled_whenPropertyFalse() {
        contextRunner
                .withPropertyValues("atom.jwt.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(JwtAutoConfiguration.class);
                    assertThat(context).doesNotHaveBean(JwtProperties.class);
                });
    }

    @Test
    void createsJwsSignerBean() {
        contextRunner.run(context ->
                assertThat(context).hasSingleBean(JWSSigner.class));
    }

    @Test
    void createsJwsVerifierBean() {
        contextRunner.run(context ->
                assertThat(context).hasSingleBean(JWSVerifier.class));
    }

    @Test
    void createsJwtServiceBean() {
        contextRunner.run(context ->
                assertThat(context).hasSingleBean(JwtService.class));
    }

    @Test
    void failsWhenSecretNotSet() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(JwtAutoConfiguration.class))
                .run(context -> assertThat(context).hasFailed());
    }

    @Test
    void failsWhenSecretEmpty() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(JwtAutoConfiguration.class))
                .withPropertyValues("atom.jwt.secret=")
                .run(context -> assertThat(context).hasFailed());
    }

    @Test
    void failsWhenSecretNotBase64() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(JwtAutoConfiguration.class))
                .withPropertyValues("atom.jwt.secret=@#$%^&*()")
                .run(context -> assertThat(context).hasFailed());
    }

    @Test
    void failsWhenSecretTooShort() {
        String shortSecret = Base64.getEncoder().encodeToString(new byte[4]);
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(JwtAutoConfiguration.class))
                .withPropertyValues("atom.jwt.secret=" + shortSecret)
                .run(context -> assertThat(context).hasFailed());
    }

    @Test
    void defaultPropertyValues() {
        contextRunner.run(context -> {
            JwtProperties properties = context.getBean(JwtProperties.class);
            assertThat(properties.getEnabled()).isTrue();
            assertThat(properties.getSecret()).isEqualTo(TEST_SECRET);
        });
    }

}
